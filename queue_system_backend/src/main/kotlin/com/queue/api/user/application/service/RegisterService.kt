package com.queue.api.user.application.service

import com.queue.api.user.application.port.`in`.RegisterUseCase
import com.queue.api.user.application.port.out.UserPort
import com.queue.api.user.domain.User
import com.queue.api.user.presentation.dto.request.RegisterRequest
import com.queue.global.exception.ServiceException
import org.springframework.stereotype.Service

@Service
class RegisterService(
    private val userPort: UserPort
) : RegisterUseCase {

    override fun register(request: RegisterRequest) {
        if (userPort.existsByStudentNo(request.studentNo)) {
            throw ServiceException.StudentNoAlreadyExistsException()
        }
        val user = User.create(
            studentNo = request.studentNo,
            password = request.password,
            name = request.name
        )
        userPort.save(user)
    }

}
