package com.queue.api.registration.application.port.`in`

import com.queue.api.registration.presentation.dto.response.RegistrationResponse

interface GetRegistrationUseCase {
    fun getMyRegistrations(studentNo: String): List<RegistrationResponse>
}
