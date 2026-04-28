package com.queue.api.user.application.port.`in`

import com.queue.api.user.presentation.dto.request.RegisterRequest

interface RegisterUseCase {
    fun register(request: RegisterRequest)
}
