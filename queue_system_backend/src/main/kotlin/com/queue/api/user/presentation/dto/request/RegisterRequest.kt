package com.queue.api.user.presentation.dto.request

data class RegisterRequest(
    val studentNo: String,
    val password: String,
    val name: String
)
