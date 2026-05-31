package com.queue.api.registration.presentation.controller

import com.queue.api.registration.application.port.`in`.RegistrationUseCase
import com.queue.api.registration.presentation.dto.request.CancelRegistrationRequest
import com.queue.api.registration.presentation.dto.request.CreateRegistrationRequest
import com.queue.api.registration.presentation.dto.response.QueueJoinResponse
import com.queue.api.registration.presentation.dto.response.RegistrationResponse
import com.queue.global.common.response.BaseResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/registration")
class RegistrationController(
    private val registrationUseCase: RegistrationUseCase
) {

    @PostMapping
    fun register(
        @RequestBody request: CreateRegistrationRequest
    ): BaseResponse<QueueJoinResponse> {
        return BaseResponse.ok(registrationUseCase.register(request))
    }

    @GetMapping
    fun getMyRegistrations(
        @RequestParam studentNo: String
    ): BaseResponse<List<RegistrationResponse>> {
        return BaseResponse.ok(registrationUseCase.getMyRegistrations(studentNo))
    }

    @DeleteMapping("/{courseId}")
    fun cancelRegistration(
        @PathVariable courseId: Long,
        @RequestBody request: CancelRegistrationRequest
    ): BaseResponse<String> {
        return BaseResponse.ok(registrationUseCase.cancelRegistration(request.studentNo, courseId))
    }
}
