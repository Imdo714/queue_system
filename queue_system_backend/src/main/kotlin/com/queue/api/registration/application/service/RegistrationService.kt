package com.queue.api.registration.application.service

import com.queue.api.registration.application.port.`in`.RegistrationUseCase
import com.queue.api.registration.application.port.out.RegistrationCommandPort
import com.queue.api.registration.application.port.out.RegistrationQueryPort
import com.queue.api.registration.application.port.out.RegistrationRedisPort
import com.queue.api.registration.domain.Registration
import com.queue.api.registration.presentation.dto.request.CreateRegistrationRequest
import com.queue.api.registration.presentation.dto.response.QueueJoinResponse
import com.queue.api.registration.presentation.dto.response.RegistrationResponse
import com.queue.global.exception.ServiceException
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger {}

@Service
class RegistrationService(
    private val registrationCommandPort: RegistrationCommandPort,
    private val registrationQueryPort: RegistrationQueryPort,
    private val registrationRedisPort: RegistrationRedisPort,
    private val registrationValidator: RegistrationValidator,
    private val courseEnrollmentManager: CourseEnrollmentManager
) : RegistrationUseCase {

    override fun register(request: CreateRegistrationRequest): QueueJoinResponse {
        val user = registrationValidator.validateAndGetUser(request.studentNo)
        val course = registrationValidator.validateAndGetCourse(request.courseId)

        registrationValidator.validateCourseIsOpen(course)
        registrationValidator.validateNoTimeConflict(user.id!!, course)

        val availableCapacity = course.maxCapacity - course.currentEnrolled
        val rank = when (val r = registrationRedisPort.joinQueue(course.id!!, request.studentNo, availableCapacity)) {
            -1L -> throw ServiceException.CourseFullException()
            -3L -> throw ServiceException.RegistrationAlreadyExistsException()
            else -> r
        }

        // Redis 등록 성공 → in-memory 저장 (Spring Batch 구현 전 임시 저장소)
        runCatching {
            registrationCommandPort.save(Registration.create(user, course))
            courseEnrollmentManager.increase(course)
        }.onFailure { ex ->
            log.error(ex) { "저장 실패 — Redis 원복: courseId=${course.id}, studentNo=${request.studentNo}" }
            registrationRedisPort.removeFromQueue(course.id!!, request.studentNo)
            throw ex
        }

        registrationRedisPort.invalidateRegistrationCache(request.studentNo)
        log.info { "수강신청 완료: studentNo=${request.studentNo}, courseId=${course.id}, 순번=${rank + 1}" }
        return QueueJoinResponse(immediate = true, rank = rank + 1)
    }

    override fun getMyRegistrations(studentNo: String): List<RegistrationResponse> {
        registrationRedisPort.getCachedRegistrations(studentNo)?.let { cached ->
            log.debug { "캐시 HIT: studentNo=$studentNo" }
            return cached
        }

        val user = registrationValidator.validateAndGetUser(studentNo)
        val responses = registrationQueryPort.findByUserId(user.id!!)
            .map { RegistrationResponse.from(it) }

        registrationRedisPort.cacheRegistrations(studentNo, responses)
        log.debug { "캐시 MISS → 캐싱: studentNo=$studentNo" }
        return responses
    }

    override fun cancelRegistration(studentNo: String, courseId: Long): String {
        val user = registrationValidator.validateAndGetUser(studentNo)
        val course = registrationValidator.validateAndGetCourse(courseId)
        val registration = registrationValidator.validateAndGetRegistration(user.id!!, course.id!!)

        registrationCommandPort.delete(registration.id!!)
        courseEnrollmentManager.decrease(course)
        registrationRedisPort.removeFromQueue(courseId, studentNo)
        registrationRedisPort.invalidateRegistrationCache(studentNo)

        log.info { "수강취소 완료: studentNo=$studentNo, courseId=$courseId" }
        return "${course.title} 수강취소가 완료되었습니다."
    }
}
