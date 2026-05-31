package com.queue.api.registration.application.port.`in`

import com.queue.api.registration.presentation.dto.request.CreateRegistrationRequest
import com.queue.api.registration.presentation.dto.response.QueueJoinResponse
import com.queue.api.registration.presentation.dto.response.RegistrationResponse

interface RegistrationUseCase {
    fun register(request: CreateRegistrationRequest): QueueJoinResponse
    fun getMyRegistrations(studentNo: String): List<RegistrationResponse>
    fun cancelRegistration(studentNo: String, courseId: Long): String
}
