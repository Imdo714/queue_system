package com.queue.api.registration.infrastructure.persistence

import com.queue.api.registration.application.port.out.RegistrationRedisPort
import com.queue.api.registration.presentation.dto.response.RegistrationResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.script.DefaultRedisScript
import org.springframework.stereotype.Repository
import tools.jackson.databind.ObjectMapper
import java.time.Duration

private val log = KotlinLogging.logger {}

/**
 * Redis Outbound Adapter.
 *
 * ## Redis 키
 *  course:capacity:{courseId}  → String     잔여 정원
 *  course:queue:{courseId}     → Sorted Set 신청자 목록 (score = 신청 시각ms)
 *  user:courses:{studentNo}    → String     JSON 캐시 (5분 TTL)
 *
 * ## 동시성
 *  모든 상태 변경은 Lua 스크립트로 원자 처리.
 */
@Repository
class RegistrationRedisPersistenceAdapter(
    private val redisTemplate: RedisTemplate<String, String>,
    private val objectMapper: ObjectMapper
) : RegistrationRedisPort {

    companion object {
        private const val CAPACITY_KEY = "course:capacity:"
        private const val QUEUE_KEY = "course:queue:"
        private const val USER_COURSES_KEY = "user:courses:"
        private val CACHE_TTL = Duration.ofMinutes(5)

        /**
         * 수강신청 원자적 Lua 스크립트.
         *
         * KEYS[1] = course:capacity:{courseId}
         * KEYS[2] = course:queue:{courseId}    (Sorted Set)
         * ARGV[1] = studentNo
         * ARGV[2] = timestamp (ms) — score
         * ARGV[3] = availableCapacity         (capacity 키 미존재 시 초기값)
         *
         * 반환: 0-based 순번 | -1: 정원 초과 | -3: 이미 신청됨
         */
        private val JOIN_SCRIPT = DefaultRedisScript<Long>().apply {
            setScriptText("""
                -- 중복 신청 검사
                if redis.call('ZSCORE', KEYS[2], ARGV[1]) ~= false then
                    return -3
                end

                -- capacity 키 없으면 초기화 (서버 재시작 복구)
                if redis.call('EXISTS', KEYS[1]) == 0 then
                    redis.call('SET', KEYS[1], ARGV[3])
                end

                -- 잔여 정원 확인
                local remaining = tonumber(redis.call('GET', KEYS[1]))
                if remaining == nil or remaining <= 0 then
                    return -1
                end

                -- 정원 차감 (동시 요청 race condition 방지)
                local newRemaining = redis.call('DECR', KEYS[1])
                if newRemaining < 0 then
                    redis.call('INCR', KEYS[1])
                    return -1
                end

                -- Sorted Set 등록 (score = 신청 시각, 낮을수록 먼저)
                redis.call('ZADD', KEYS[2], ARGV[2], ARGV[1])
                return redis.call('ZRANK', KEYS[2], ARGV[1])
            """.trimIndent())
            resultType = Long::class.java
        }

        /**
         * 수강 취소 원자적 Lua 스크립트 — ZREM + 정원 INCR.
         *
         * KEYS[1] = course:capacity:{courseId}
         * KEYS[2] = course:queue:{courseId}
         * ARGV[1] = studentNo
         *
         * 반환: 1 = 취소 성공 | 0 = 신청 내역 없음
         */
        private val CANCEL_SCRIPT = DefaultRedisScript<Long>().apply {
            setScriptText("""
                if redis.call('ZSCORE', KEYS[2], ARGV[1]) ~= false then
                    redis.call('ZREM', KEYS[2], ARGV[1])
                    redis.call('INCR', KEYS[1])
                    return 1
                end
                return 0
            """.trimIndent())
            resultType = Long::class.java
        }
    }

    // ── 수강신청 / 취소 ──────────────────────────────────────────────────────

    override fun joinQueue(courseId: Long, studentNo: String, availableCapacity: Int): Long {
        val timestamp = System.currentTimeMillis()
        return redisTemplate.execute(
            JOIN_SCRIPT,
            listOf("$CAPACITY_KEY$courseId", "$QUEUE_KEY$courseId"),
            studentNo,
            timestamp.toString(),
            availableCapacity.toString()
        ) ?: error("Redis JOIN 스크립트 null 반환 — courseId=$courseId, studentNo=$studentNo")
    }

    override fun removeFromQueue(courseId: Long, studentNo: String): Boolean {
        val result = redisTemplate.execute(
            CANCEL_SCRIPT,
            listOf("$CAPACITY_KEY$courseId", "$QUEUE_KEY$courseId"),
            studentNo
        ) ?: 0L
        log.debug { "Redis 취소: courseId=$courseId, studentNo=$studentNo, result=$result" }
        return result == 1L
    }

    // ── 순번 조회 ─────────────────────────────────────────────────────────────

    override fun getQueueRank(courseId: Long, studentNo: String): Long? =
        redisTemplate.opsForZSet().rank("$QUEUE_KEY$courseId", studentNo)

    override fun getQueueSize(courseId: Long): Long =
        redisTemplate.opsForZSet().size("$QUEUE_KEY$courseId") ?: 0L

    // ── Cache-Aside ───────────────────────────────────────────────────────────

    override fun getCachedRegistrations(studentNo: String): List<RegistrationResponse>? {
        val json = redisTemplate.opsForValue().get("$USER_COURSES_KEY$studentNo") ?: return null
        return objectMapper.readValue(json, Array<RegistrationResponse>::class.java).toList()
    }

    override fun cacheRegistrations(studentNo: String, responses: List<RegistrationResponse>) {
        val json = objectMapper.writeValueAsString(responses)
        redisTemplate.opsForValue().set("$USER_COURSES_KEY$studentNo", json, CACHE_TTL)
    }

    override fun invalidateRegistrationCache(studentNo: String) {
        redisTemplate.delete("$USER_COURSES_KEY$studentNo")
    }
}
