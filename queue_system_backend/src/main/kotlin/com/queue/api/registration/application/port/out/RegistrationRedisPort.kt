package com.queue.api.registration.application.port.out

import com.queue.api.registration.presentation.dto.response.RegistrationResponse

/**
 * Redis 전용 Outbound Port.
 *
 * ## Redis 키 설계
 *  course:capacity:{courseId}  → String     잔여 정원
 *  course:queue:{courseId}     → Sorted Set 신청 순서 기록 (score = 신청 시각)
 *  user:courses:{studentNo}    → String     JSON 캐시 (5분 TTL)
 *
 * ## 흐름
 *  1. joinQueue    : 정원 체크 + Sorted Set 등록 + 정원 차감 (Lua 원자 처리)
 *  2. removeFromQueue : 취소 — ZREM + 정원 반납 (Lua 원자 처리)
 *  3. getQueueRank / getQueueSize : 순번·인원 조회
 */
interface RegistrationRedisPort {

    /**
     * 수강신청 원자적 처리.
     * @param availableCapacity 잔여 정원 (capacity 키 미존재 시 초기값)
     * @return 0-based 순번(>=0) | -1: 정원 초과 | -3: 이미 신청됨
     */
    fun joinQueue(courseId: Long, studentNo: String, availableCapacity: Int): Long

    /**
     * 수강 취소 원자적 처리 — ZREM + 정원 INCR.
     * @return true: 취소 성공 | false: 신청 내역 없음
     */
    fun removeFromQueue(courseId: Long, studentNo: String): Boolean

    /** 0-based 신청 순번. 미신청이면 null */
    fun getQueueRank(courseId: Long, studentNo: String): Long?

    /** 전체 신청 인원 수 */
    fun getQueueSize(courseId: Long): Long

    fun getCachedRegistrations(studentNo: String): List<RegistrationResponse>?
    fun cacheRegistrations(studentNo: String, responses: List<RegistrationResponse>)
    fun invalidateRegistrationCache(studentNo: String)
}
