package com.queue.api.registration.presentation.dto.request

data class CreateRegistrationRequest(
    val studentNo: String,
    val courseId: Long
)
