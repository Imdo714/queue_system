package com.queue.api.registration.application.port.`in`

interface CancelRegistrationUseCase {
    fun cancelRegistration(studentNo: String, courseId: Long): String
}
