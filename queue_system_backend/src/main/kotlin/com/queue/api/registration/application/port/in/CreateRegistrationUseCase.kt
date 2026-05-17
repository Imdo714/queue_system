package com.queue.api.registration.application.port.`in`

import com.queue.api.registration.presentation.dto.request.CreateRegistrationRequest

interface CreateRegistrationUseCase {
    fun register(request: CreateRegistrationRequest): String
}
