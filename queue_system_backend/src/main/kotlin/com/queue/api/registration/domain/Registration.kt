package com.queue.api.registration.domain

import com.queue.api.course.domain.Course
import com.queue.api.user.domain.User
import java.time.LocalDateTime

class Registration(
    val id: Long? = null,
    val user: User,
    val course: Course,
    val registrationDate: LocalDateTime = LocalDateTime.now()
) {
    companion object {
        fun create(user: User, course: Course) = Registration(user = user, course = course)
    }
}
