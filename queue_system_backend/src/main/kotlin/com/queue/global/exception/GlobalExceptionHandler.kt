package com.queue.global.exception

import com.queue.global.common.response.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException::class)
    protected fun handleServiceException(e: ServiceException): ResponseEntity<ErrorResponse> {
        val errorCode = e.errorCode
        val status = HttpStatus.valueOf(errorCode.httpStatus)

        return ResponseEntity
            .status(status)
            .body(ErrorResponse.of(status, errorCode))
    }

}