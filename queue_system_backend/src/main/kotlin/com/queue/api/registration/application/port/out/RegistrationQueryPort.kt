package com.queue.api.registration.application.port.out

import com.queue.api.registration.domain.Registration

interface RegistrationQueryPort {
    fun findById(id: Long): Registration?
    fun findByUserIdAndCourseId(userId: Long, courseId: Long): Registration?
    fun findByUserId(userId: Long): List<Registration>
}
