package com.queue.global.common.response

import org.springframework.http.HttpStatus

class BaseResponse<T>(
    val code: Int,
    val status: HttpStatus,
    val message: String,
    val data: T? = null
){
    companion object {
        fun <T> of(
            status: HttpStatus,
            message: String? = null,
            data: T? = null
        ): BaseResponse<T> {
            return BaseResponse(
                code = status.value(),
                status = status,
                message = message ?: status.name,
                data = data
            )
        }

        fun <T> ok(data: T? = null): BaseResponse<T> {
            return of(status = HttpStatus.OK, null, data = data)
        }
    }
}
