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
 * Redis Outbound Adapter — infrastructure 패키지에 위치.
 *
 * RegistrationRedisPort 를 구현하며, 모든 Redis 기술 세부사항을 이 클래스 안에 캡슐화한다.
 *  - RegistrationService 는 이 클래스의 존재를 모른다 (포트 인터페이스만 안다).
 *  - JSON 직렬화/역직렬화, Lua 스크립트, Redis 키 네이밍 등이 여기서 처리된다.
 *
 * ## Redis 키 설계
 *  course:capacity:{courseId}   → String (잔여 정원 정수)
 *  course:registered:{courseId} → Set<String> (등록된 userId)
 *  user:courses:{studentNo}     → String (JSON, 5분 TTL)
 *
 * ## 동시성 제어 — Lua 스크립트
 * Redis 는 싱글 스레드로 명령을 처리하므로, Lua 스크립트 실행 중에는
 * 다른 클라이언트 명령이 끼어들 수 없다 → 별도 분산 락 없이 Race Condition 방지.
 *
 * ### Redisson 분산 락 대안 (복잡한 임계 영역에 적합)
 * 여러 Redis 명령과 외부 작업(HTTP 호출 등)을 하나의 임계 영역으로 묶을 때는
 * Redisson RLock 사용을 권장한다.
 * ```kotlin
 * val lock = redissonClient.getLock("lock:course:$courseId")
 * if (!lock.tryLock(1, 5, TimeUnit.SECONDS)) throw ServiceException.CourseFullException()
 * try { /* 복합 작업 */ } finally { lock.unlock() }
 * ```
 */
@Repository
class RegistrationRedisPersistenceAdapter(
    private val redisTemplate: RedisTemplate<String, String>,
    private val objectMapper: ObjectMapper
) : RegistrationRedisPort {

    companion object {
        private const val CAPACITY_KEY = "course:capacity:"
        private const val REGISTERED_KEY = "course:registered:"
        private const val USER_COURSES_KEY = "user:courses:"
        private val CACHE_TTL = Duration.ofMinutes(5)

        /**
         * 수강신청 원자적 Lua 스크립트.
         *
         * KEYS[1] = course:capacity:{courseId}   (잔여 정원)
         * KEYS[2] = course:registered:{courseId} (등록 userId 집합)
         * ARGV[1] = userId
         * ARGV[2] = availableCapacity (키 미존재 시 초기값 — 서버 재시작 자동 복구)
         *
         * 반환값: 남은 정원(>= 0) | -1: 정원 초과 | -2: 중복 신청
         */
        private val REGISTER_SCRIPT = DefaultRedisScript<Long>().apply {
            setScriptText("""
                -- 정원 키 미존재 시 초기화 (서버 재시작 후 첫 요청 자동 복구)
                if redis.call('EXISTS', KEYS[1]) == 0 then
                    redis.call('SET', KEYS[1], ARGV[2])
                end

                -- 중복 신청 검사 (SISMEMBER: O(1))
                if redis.call('SISMEMBER', KEYS[2], ARGV[1]) == 1 then
                    return -2
                end

                -- 잔여 정원 확인
                local remaining = tonumber(redis.call('GET', KEYS[1]))
                if remaining <= 0 then
                    return -1
                end

                -- 정원 차감 (DECR 원자적 연산)
                local newRemaining = redis.call('DECR', KEYS[1])
                if newRemaining < 0 then
                    -- 동시 요청으로 음수가 된 경우 즉시 원복
                    redis.call('INCR', KEYS[1])
                    return -1
                end

                -- 등록 집합에 추가
                redis.call('SADD', KEYS[2], ARGV[1])
                return newRemaining
            """.trimIndent())
            resultType = Long::class.java
        }

        /**
         * 수강 취소 원자적 Lua 스크립트.
         *
         * KEYS[1] = course:capacity:{courseId}
         * KEYS[2] = course:registered:{courseId}
         * ARGV[1] = userId
         *
         * userId 가 집합에 없어도 멱등하게 동작한다.
         */
        private val CANCEL_SCRIPT = DefaultRedisScript<Long>().apply {
            setScriptText("""
                if redis.call('SISMEMBER', KEYS[2], ARGV[1]) == 1 then
                    redis.call('INCR', KEYS[1])
                    redis.call('SREM', KEYS[2], ARGV[1])
                end
                return 1
            """.trimIndent())
            resultType = Long::class.java
        }
    }

    // ── 동시성 제어 ───────────────────────────────────────────────────────────

    override fun tryRegister(courseId: Long, userId: Long, availableCapacity: Int): Long {
        return redisTemplate.execute(
            REGISTER_SCRIPT,
            listOf("$CAPACITY_KEY$courseId", "$REGISTERED_KEY$courseId"),
            userId.toString(),
            availableCapacity.toString()
        ) ?: error("Redis REGISTER 스크립트가 null 반환 — courseId=$courseId, userId=$userId")
    }

    override fun cancelRegistration(courseId: Long, userId: Long) {
        redisTemplate.execute(
            CANCEL_SCRIPT,
            listOf("$CAPACITY_KEY$courseId", "$REGISTERED_KEY$courseId"),
            userId.toString()
        )
        log.debug { "Redis 취소 복구 완료: courseId=$courseId, userId=$userId" }
    }

    // ── Cache-Aside (JSON 직렬화는 이 클래스 내부에서만 처리) ─────────────────

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
