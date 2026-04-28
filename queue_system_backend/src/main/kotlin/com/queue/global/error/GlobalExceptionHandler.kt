package com.queue.global.error

import com.queue.global.common.response.BaseResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(e: RuntimeException): BaseResponse<Unit> {
        return BaseResponse.of(
            status = HttpStatus.BAD_REQUEST,
            message = e.message
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): BaseResponse<Unit> {
        return BaseResponse.of(
            status = HttpStatus.INTERNAL_SERVER_ERROR,
            message = e.message
        )
    }
}
