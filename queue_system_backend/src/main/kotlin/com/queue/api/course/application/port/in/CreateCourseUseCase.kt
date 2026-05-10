package com.queue.api.course.application.port.`in`

import com.queue.api.course.presentation.dto.request.CreateCourseRequest

interface CreateCourseUseCase {
    fun createCourse(request: CreateCourseRequest)
}
