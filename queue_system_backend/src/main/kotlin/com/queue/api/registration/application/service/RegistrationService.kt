package com.queue.api.registration.application.service

import com.queue.api.registration.application.port.`in`.CancelRegistrationUseCase
import com.queue.api.registration.application.port.`in`.CreateRegistrationUseCase
import com.queue.api.registration.application.port.`in`.GetRegistrationUseCase
import com.queue.api.registration.application.port.out.RegistrationPort
import com.queue.api.registration.domain.Registration
import com.queue.api.registration.presentation.dto.request.CreateRegistrationRequest
import com.queue.api.registration.presentation.dto.response.RegistrationResponse
import org.springframework.stereotype.Service

@Service
class RegistrationService(
    private val registrationPort: RegistrationPort,
    private val registrationValidator: RegistrationValidator,
    private val courseEnrollmentManager: CourseEnrollmentManager
) : CreateRegistrationUseCase, GetRegistrationUseCase, CancelRegistrationUseCase {

    override fun register(request: CreateRegistrationRequest): String {
        val user = registrationValidator.validateAndGetUser(request.studentNo)
        val course = registrationValidator.validateAndGetCourse(request.courseId)

        registrationValidator.validateCourseIsOpen(course)
        registrationValidator.validateNotDuplicated(user.id!!, course.id!!)
        registrationValidator.validateNoTimeConflict(user.id!!, course)
        registrationValidator.validateHasCapacity(course)

        registrationPort.save(Registration.create(user, course))
        courseEnrollmentManager.increase(course)

        return "${course.title} 수강신청이 완료되었습니다."
    }

    override fun getMyRegistrations(studentNo: String): List<RegistrationResponse> {
        val user = registrationValidator.validateAndGetUser(studentNo)

        return registrationPort.findByUserId(user.id!!)
            .map { RegistrationResponse.from(it) }
    }

    override fun cancelRegistration(studentNo: String, courseId: Long): String {
        val user = registrationValidator.validateAndGetUser(studentNo)
        val course = registrationValidator.validateAndGetCourse(courseId)
        val registration = registrationValidator.validateAndGetRegistration(user.id!!, course.id!!)

        registrationPort.delete(registration.id!!)
        courseEnrollmentManager.decrease(course)

        return "${course.title} 수강취소가 완료되었습니다."
    }

}
