package com.queue.api.user.application.port.`in`

import com.queue.api.user.presentation.dto.request.LoginRequest
import com.queue.api.user.presentation.dto.response.LoginResponse

interface LoginUseCase {
    fun login(request: LoginRequest): LoginResponse
}
