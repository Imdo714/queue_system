package com.queue.api.course.application.port.`in`

import com.queue.api.course.presentation.dto.response.CourseResponse

interface GetCourseUseCase {
    fun getCourses(): List<CourseResponse>
    fun getCourse(courseId: Long): CourseResponse
}
