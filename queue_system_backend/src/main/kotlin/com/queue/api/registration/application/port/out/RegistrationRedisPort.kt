package com.queue.api.registration.application.port.out

import com.queue.api.registration.presentation.dto.response.RegistrationResponse

/**
 * Redis 전용 Outbound Port.
 *
 * RegistrationService 는 이 인터페이스만 의존하며, JSON 직렬화·Redis 명령 등
 * 구체적인 인프라 세부사항은 전혀 알지 못한다.
 * 구현체(RegistrationRedisPersistenceAdapter)가 infrastructure 패키지에서 담당한다.
 *
 * ## 역할 분담
 *  - tryRegister      : 수강신청 원자적 시도 (Lua 스크립트 — 중복·정원 동시 검사)
 *  - cancelRegistration: 수강 취소 원자적 복구 (정원 INCR + 등록 집합 SREM)
 *  - 캐시 3종         : Cache-Aside 패턴 (GET / SET-with-TTL / DEL)
 */
interface RegistrationRedisPort {

    /**
     * 수강신청 원자적 시도.
     *
     * @return 남은 잔여 정원(>= 0): 성공 | -1: 정원 초과 | -2: 중복 신청
     */
    fun tryRegister(courseId: Long, userId: Long, availableCapacity: Int): Long

    /**
     * 수강 취소 원자적 복구.
     * 정원 INCR + 등록 집합 SREM 을 단일 Lua 스크립트로 처리한다.
     */
    fun cancelRegistration(courseId: Long, userId: Long)

    /** Cache-Aside: 캐시 조회 — miss 이면 null */
    fun getCachedRegistrations(studentNo: String): List<RegistrationResponse>?

    /** Cache-Aside: 5분 TTL 로 응답 목록 캐싱 */
    fun cacheRegistrations(studentNo: String, responses: List<RegistrationResponse>)

    /** 수강신청·취소 후 캐시 무효화 */
    fun invalidateRegistrationCache(studentNo: String)
}
