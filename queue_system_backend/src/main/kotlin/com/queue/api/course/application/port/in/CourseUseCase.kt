package com.queue.api.course.application.port.`in`

import com.queue.api.course.presentation.dto.request.CreateCourseRequest

interface CourseUseCase {
    fun createCourse(request: CreateCourseRequest)
    fun deleteCourse(studentNo: String, courseId: Long)
}