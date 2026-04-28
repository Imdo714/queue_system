package com.queue.api.user.presentation.controller

import com.queue.api.user.application.port.`in`.LoginUseCase
import com.queue.api.user.application.port.`in`.RegisterUseCase
import com.queue.api.user.presentation.dto.request.LoginRequest
import com.queue.api.user.presentation.dto.request.RegisterRequest
import com.queue.api.user.presentation.dto.response.LoginResponse
import com.queue.global.common.response.BaseResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase
) {

    @PostMapping("/register")
    fun register(
        @RequestBody request: RegisterRequest
    ): BaseResponse<Unit> {
        registerUseCase.register(request)
        return BaseResponse.ok()
    }

    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest
    ): BaseResponse<LoginResponse> {
        return BaseResponse.ok(loginUseCase.login(request))
    }

}
