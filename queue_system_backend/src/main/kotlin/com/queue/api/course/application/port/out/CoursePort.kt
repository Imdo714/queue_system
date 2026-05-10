package com.queue.api.course.application.port.out

import com.queue.api.course.domain.Course

interface CoursePort {
    fun save(course: Course): Course
    fun findById(id: Long): Course?
    fun findByCourseCode(courseCode: String): Course?
    fun existsByCourseCode(courseCode: String): Boolean
    fun delete(id: Long)
}
