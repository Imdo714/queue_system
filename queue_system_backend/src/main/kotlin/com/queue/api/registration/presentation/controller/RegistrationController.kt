package com.queue.api.registration.presentation.controller

import com.queue.api.registration.application.port.`in`.CancelRegistrationUseCase
import com.queue.api.registration.application.port.`in`.CreateRegistrationUseCase
import com.queue.api.registration.application.port.`in`.GetRegistrationUseCase
import com.queue.api.registration.presentation.dto.request.CancelRegistrationRequest
import com.queue.api.registration.presentation.dto.request.CreateRegistrationRequest
import com.queue.api.registration.presentation.dto.response.RegistrationResponse
import com.queue.global.common.response.BaseResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/registration")
class RegistrationController(
    private val createRegistrationUseCase: CreateRegistrationUseCase,
    private val getRegistrationUseCase: GetRegistrationUseCase,
    private val cancelRegistrationUseCase: CancelRegistrationUseCase
) {

    @PostMapping
    fun register(
        @RequestBody request: CreateRegistrationRequest
    ): BaseResponse<String> {
        return BaseResponse.ok(createRegistrationUseCase.register(request))
    }

    @GetMapping
    fun getMyRegistrations(
        @RequestParam studentNo: String
    ): BaseResponse<List<RegistrationResponse>> {
        return BaseResponse.ok(getRegistrationUseCase.getMyRegistrations(studentNo))
    }

    @DeleteMapping("/{courseId}")
    fun cancelRegistration(
        @PathVariable courseId: Long,
        @RequestBody request: CancelRegistrationRequest
    ): BaseResponse<String> {
        return BaseResponse.ok(cancelRegistrationUseCase.cancelRegistration(request.studentNo, courseId))
    }
}
