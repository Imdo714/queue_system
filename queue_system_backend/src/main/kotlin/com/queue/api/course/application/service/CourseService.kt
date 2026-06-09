package com.queue.api.course.application.service

import com.queue.api.course.application.port.`in`.CourseUseCase
import com.queue.api.course.application.port.`in`.GetCourseUseCase
import com.queue.api.course.application.port.out.CoursePort
import com.queue.api.course.domain.Course
import com.queue.api.course.presentation.dto.request.CourseUpdateRequest
import com.queue.api.course.presentation.dto.request.CreateCourseRequest
import com.queue.api.course.presentation.dto.response.CourseResponse
import com.queue.api.registration.application.port.out.RegistrationCommandPort
import com.queue.api.user.application.port.out.UserPort
import com.queue.global.common.enums.Role
import com.queue.global.exception.ServiceException
import org.springframework.stereotype.Service

@Service
class CourseService(
    private val coursePort: CoursePort,
    private val userPort: UserPort,
    private val registrationCommandPort: RegistrationCommandPort
) : CourseUseCase, GetCourseUseCase {

    override fun createCourse(request: CreateCourseRequest) {
        validateAdmin(request.studentNo)

        if (coursePort.existsByCourseCode(request.courseCode)) {
            throw ServiceException.CourseAlreadyExistsException()
        }

        val course = Course.create(
            courseCode = request.courseCode,
            title = request.title,
            maxCapacity = request.maxCapacity,
            dayOfWeek = request.dayOfWeek,
            startTime = request.startTime,
            endTime = request.endTime
        )

        coursePort.save(course)
    }

    override fun updateCourse(courseId: Long, request: CourseUpdateRequest) {
        validateAdmin(request.studentNo)

        val course = coursePort.findById(courseId) ?: throw ServiceException.CourseNotFoundException()

        val updated = course.update(
            title = request.title,
            maxCapacity = request.maxCapacity,
            dayOfWeek = request.dayOfWeek,
            startTime = request.startTime,
            endTime = request.endTime
        )

        coursePort.save(updated)
    }

    override fun deleteCourse(studentNo: String, courseId: Long) {
        validateAdmin(studentNo)

        coursePort.findById(courseId) ?: throw ServiceException.CourseNotFoundException()

        registrationCommandPort.deleteByCourseId(courseId)
        coursePort.delete(courseId)
    }

    override fun getCourses(): List<CourseResponse> {
        return coursePort.findAll().map { CourseResponse.from(it) }
    }

    override fun getCourse(courseId: Long): CourseResponse {
        val course = coursePort.findById(courseId) ?: throw ServiceException.CourseNotFoundException()
        return CourseResponse.from(course)
    }

    private fun validateAdmin(studentNo: String) {
        val user = userPort.findByStudentNo(studentNo) ?: throw ServiceException.UserNotFoundException()
        if (user.role != Role.ADMIN) {
            throw ServiceException.UnauthorizedException()
        }
    }
}
