package com.queue.api.registration.application.service

import com.queue.api.course.application.port.out.CoursePort
import com.queue.api.course.domain.Course
import org.springframework.stereotype.Component

@Component
class CourseEnrollmentManager(
    private val coursePort: CoursePort
) {

    fun increase(course: Course) {
        course.increaseEnrolled()
        coursePort.save(course)
    }

    fun decrease(course: Course) {
        course.decreaseEnrolled()
        coursePort.save(course)
    }
}
