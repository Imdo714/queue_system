package com.queue.api.registration.application.service

import com.queue.api.course.application.port.out.CoursePort
import com.queue.api.course.domain.Course
import com.queue.api.registration.application.port.out.RegistrationPort
import com.queue.api.registration.domain.Registration
import com.queue.api.user.application.port.out.UserPort
import com.queue.api.user.domain.User
import com.queue.global.common.enums.CourseStatus
import com.queue.global.exception.ServiceException
import org.springframework.stereotype.Component

@Component
class RegistrationValidator(
    private val userPort: UserPort,
    private val coursePort: CoursePort,
    private val registrationPort: RegistrationPort
) {

    fun validateAndGetUser(studentNo: String): User {
        return userPort.findByStudentNo(studentNo)
            ?: throw ServiceException.UserNotFoundException()
    }

    fun validateAndGetCourse(courseId: Long): Course {
        return coursePort.findById(courseId)
            ?: throw ServiceException.CourseNotFoundException()
    }

    fun validateCourseIsOpen(course: Course) {
        if (course.status == CourseStatus.TERMINATED) {
            throw ServiceException.CourseClosedException()
        }
    }

    fun validateNotDuplicated(userId: Long, courseId: Long) {
        if (registrationPort.findByUserIdAndCourseId(userId, courseId) != null) {
            throw ServiceException.RegistrationAlreadyExistsException()
        }
    }

    fun validateHasCapacity(course: Course) {
        if (course.currentEnrolled >= course.maxCapacity) {
            throw ServiceException.CourseFullException()
        }
    }

    fun validateAndGetRegistration(userId: Long, courseId: Long): Registration {
        return registrationPort.findByUserIdAndCourseId(userId, courseId)
            ?: throw ServiceException.RegistrationNotFoundException()
    }

    fun validateNoTimeConflict(userId: Long, newCourse: Course) {
        val hasConflict = registrationPort.findByUserId(userId)
            .map { it.course }
            .filter { it.dayOfWeek == newCourse.dayOfWeek }
            .any { existing ->
                existing.startTime < newCourse.endTime && newCourse.startTime < existing.endTime
            }

        if (hasConflict) {
            throw ServiceException.RegistrationTimeConflictException()
        }
    }
}
