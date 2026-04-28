package com.queue.global.common.response

import com.queue.global.exception.enums.ErrorCode
import org.springframework.http.HttpStatus

class ErrorResponse(
    val code: Int,
    val status: HttpStatus,
    val message: String,
    val data: String? = null
) {
    companion object {
        fun of(
            status: HttpStatus,
            errorCode: ErrorCode
        ): ErrorResponse {
            return ErrorResponse(
                code = status.value(),
                status = status,
                message = errorCode.message,
                data = null
            )
        }

        fun of(
            status: HttpStatus,
            message: String
        ): ErrorResponse {
            return ErrorResponse(
                code = status.value(),
                status = status,
                message = message,
                data = null
            )
        }
    }
}
