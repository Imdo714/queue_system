package com.queue.api.registration.application.port.out

import com.queue.api.registration.domain.Registration

interface RegistrationCommandPort {
    fun save(registration: Registration): Registration
    fun delete(id: Long)
    fun deleteByCourseId(courseId: Long)
}
