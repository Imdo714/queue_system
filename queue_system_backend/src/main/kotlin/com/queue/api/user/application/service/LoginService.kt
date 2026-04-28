package com.queue.api.user.application.service

import com.queue.api.user.application.port.`in`.LoginUseCase
import com.queue.api.user.application.port.out.UserPort
import com.queue.api.user.presentation.dto.request.LoginRequest
import com.queue.api.user.presentation.dto.response.LoginResponse
import com.queue.global.exception.ServiceException
import org.springframework.stereotype.Service

@Service
class LoginService(
    private val userPort: UserPort
) : LoginUseCase {

    override fun login(request: LoginRequest): LoginResponse {
        val user = userPort.findByStudentNo(request.studentNo)
            ?: throw ServiceException.UserNotFoundException()
        
        if (user.password != request.password) {
            throw ServiceException.InvalidPasswordException()
        }

        return LoginResponse.of(user.studentNo, user.name)
    }

}
