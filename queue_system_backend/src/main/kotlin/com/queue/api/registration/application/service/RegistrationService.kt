package com.queue.api.registration.application.service

import com.queue.api.registration.application.port.`in`.CancelRegistrationUseCase
import com.queue.api.registration.application.port.`in`.CreateRegistrationUseCase
import com.queue.api.registration.application.port.`in`.GetRegistrationUseCase
import com.queue.api.registration.application.port.out.RegistrationCommandPort
import com.queue.api.registration.application.port.out.RegistrationQueryPort
import com.queue.api.registration.application.port.out.RegistrationRedisPort
import com.queue.api.registration.domain.Registration
import com.queue.api.registration.presentation.dto.request.CreateRegistrationRequest
import com.queue.api.registration.presentation.dto.response.RegistrationResponse
import com.queue.global.exception.ServiceException
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger {}

/**
 * 수강신청 유스케이스 구현체.
 *
 * 오직 비즈니스 흐름만 담당한다.
 * Redis 명령, JSON 직렬화, ConcurrentHashMap 접근 등 인프라 세부사항은
 * 모두 포트(인터페이스) 뒤에 숨겨져 있으며 이 클래스는 알지 못한다.
 *
 * ┌─────────────────────────────────────────────────────────┐
 * │  수강신청 (register)                                     │
 * │  1. 사용자·강의 유효성 검증                              │
 * │  2. 강의 상태·시간 충돌 검증                             │
 * │  3. Redis 원자적 시도 → 중복·정원 초과 즉시 차단         │
 * │  4. 인메모리 저장 + 강의 정원 업데이트                   │
 * │     └─ 저장 실패 시 Redis 보상(cancelRegistration)       │
 * │  5. 캐시 무효화                                          │
 * ├─────────────────────────────────────────────────────────┤
 * │  수강 취소 (cancelRegistration)                          │
 * │  1. 사용자·강의·신청 내역 검증                           │
 * │  2. 인메모리 삭제 + 강의 정원 업데이트                   │
 * │  3. Redis 원자적 정원 복구 (INCR + SREM)                 │
 * │  4. 캐시 무효화                                          │
 * ├─────────────────────────────────────────────────────────┤
 * │  내 강의 목록 (getMyRegistrations) — Cache-Aside         │
 * │  1. Redis 캐시 HIT → 즉시 반환                           │
 * │  2. Redis 캐시 MISS → 인메모리 조회 → 캐싱 → 반환        │
 * └─────────────────────────────────────────────────────────┘
 */
@Service
class RegistrationService(
    private val registrationCommandPort: RegistrationCommandPort,
    private val registrationQueryPort: RegistrationQueryPort,
    private val registrationRedisPort: RegistrationRedisPort,
    private val registrationValidator: RegistrationValidator,
    private val courseEnrollmentManager: CourseEnrollmentManager
) : CreateRegistrationUseCase, GetRegistrationUseCase, CancelRegistrationUseCase {

    override fun register(request: CreateRegistrationRequest): String {
        val user = registrationValidator.validateAndGetUser(request.studentNo)
        val course = registrationValidator.validateAndGetCourse(request.courseId)

        registrationValidator.validateCourseIsOpen(course)
        registrationValidator.validateNoTimeConflict(user.id!!, course)

        // Redis 원자적 시도: 중복·정원 초과를 단일 Lua 스크립트로 처리
        val availableCapacity = course.maxCapacity - course.currentEnrolled
        when (registrationRedisPort.tryRegister(course.id!!, user.id!!, availableCapacity)) {
            -2L -> throw ServiceException.RegistrationAlreadyExistsException()
            -1L -> throw ServiceException.CourseFullException()
        }

        // Redis 예약 성공 → 인메모리 저장 (실패 시 Redis 보상 롤백)
        runCatching {
            registrationCommandPort.save(Registration.create(user, course))
            courseEnrollmentManager.increase(course)
        }.onFailure { ex ->
            log.error(ex) { "저장 실패 — Redis 상태 원복: courseId=${course.id}, userId=${user.id}" }
            registrationRedisPort.cancelRegistration(course.id!!, user.id!!)
            throw ex
        }

        registrationRedisPort.invalidateRegistrationCache(request.studentNo)
        log.info { "수강신청 완료: studentNo=${request.studentNo}, courseId=${course.id}" }
        return "${course.title} 수강신청이 완료되었습니다."
    }

    override fun getMyRegistrations(studentNo: String): List<RegistrationResponse> {
        // Cache-Aside: Redis 캐시 우선 조회
        registrationRedisPort.getCachedRegistrations(studentNo)?.let { cached ->
            log.debug { "캐시 HIT: studentNo=$studentNo" }
            return cached
        }

        // 캐시 MISS: 인메모리 조회 후 Redis 캐싱
        val user = registrationValidator.validateAndGetUser(studentNo)
        val responses = registrationQueryPort.findByUserId(user.id!!)
            .map { RegistrationResponse.from(it) }

        registrationRedisPort.cacheRegistrations(studentNo, responses)
        log.debug { "캐시 MISS → 캐싱: studentNo=$studentNo, count=${responses.size}" }
        return responses
    }

    override fun cancelRegistration(studentNo: String, courseId: Long): String {
        val user = registrationValidator.validateAndGetUser(studentNo)
        val course = registrationValidator.validateAndGetCourse(courseId)
        val registration = registrationValidator.validateAndGetRegistration(user.id!!, course.id!!)

        // 인메모리 삭제 먼저 — 인메모리 저장소가 Source of Truth
        registrationCommandPort.delete(registration.id!!)
        courseEnrollmentManager.decrease(course)

        // 인메모리 삭제 성공 후 Redis 원자적 정원 복구
        registrationRedisPort.cancelRegistration(course.id!!, user.id!!)
        registrationRedisPort.invalidateRegistrationCache(studentNo)

        log.info { "수강취소 완료: studentNo=$studentNo, courseId=$courseId" }
        return "${course.title} 수강취소가 완료되었습니다."
    }
}
