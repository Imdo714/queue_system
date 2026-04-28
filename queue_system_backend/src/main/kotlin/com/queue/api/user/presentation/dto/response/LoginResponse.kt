package com.queue.api.user.presentation.dto.response

data class LoginResponse(
    val studentNo: String,
    val name: String
) {
    companion object {
        fun of(studentNo: String, name: String): LoginResponse {
            return LoginResponse(
                studentNo = studentNo,
                name = name
            )
        }
    }
}