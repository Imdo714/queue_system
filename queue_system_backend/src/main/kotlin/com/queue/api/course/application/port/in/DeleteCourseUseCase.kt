package com.queue.api.course.application.port.`in`

interface DeleteCourseUseCase {
    fun deleteCourse(adminId: String, courseId: Long)
}
