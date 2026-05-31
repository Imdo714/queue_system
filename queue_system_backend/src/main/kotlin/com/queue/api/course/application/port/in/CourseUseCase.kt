package com.queue.api.course.application.port.`in`

import com.queue.api.course.presentation.dto.request.CourseUpdateRequest
import com.queue.api.course.presentation.dto.request.CreateCourseRequest

interface CourseUseCase {
    fun createCourse(request: CreateCourseRequest)
    fun deleteCourse(studentNo: String, courseId: Long)
    fun updateCourse(courseId: Long, request: CourseUpdateRequest)
}