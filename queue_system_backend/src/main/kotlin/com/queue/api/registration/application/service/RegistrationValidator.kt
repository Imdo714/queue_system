package com.queue.api.registration.application.service

import com.queue.api.course.application.port.out.CoursePort
import com.queue.api.course.domain.Course
import com.queue.api.registration.application.port.out.RegistrationQueryPort
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
    private val registrationQueryPort: RegistrationQueryPort
) {

    fun validateAndGetUser(studentNo: String): User =
        userPort.findByStudentNo(studentNo) ?: throw ServiceException.UserNotFoundException()

    fun validateAndGetCourse(courseId: Long): Course =
        coursePort.findById(courseId) ?: throw ServiceException.CourseNotFoundException()

    fun validateCourseIsOpen(course: Course) {
        if (course.status == CourseStatus.TERMINATED) {
            throw ServiceException.CourseClosedException()
        }
    }

    /**
     * 수강신청 취소 시 등록 내역 존재 여부 검증.
     * 중복 신청 방지는 Redis 원자적 연산(SISMEMBER)이 담당하므로 여기서는 처리하지 않는다.
     */
    fun validateAndGetRegistration(userId: Long, courseId: Long): Registration =
        registrationQueryPort.findByUserIdAndCourseId(userId, courseId)
            ?: throw ServiceException.RegistrationNotFoundException()

    /**
     * 같은 요일·시간대에 이미 수강 중인 강의가 있는지 검사.
     * 겹침 조건: 기존 강의 시작 < 신규 강의 종료 AND 신규 강의 시작 < 기존 강의 종료
     */
    fun validateNoTimeConflict(userId: Long, newCourse: Course) {
        val hasConflict = registrationQueryPort.findByUserId(userId)
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
