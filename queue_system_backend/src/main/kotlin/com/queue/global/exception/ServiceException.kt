package com.queue.global.exception

import com.queue.global.exception.enums.ErrorCode

open class ServiceException (
    val errorCode: ErrorCode,
    cause: Throwable? = null
) : RuntimeException(errorCode.message, cause) {

    class UserNotFoundException : ServiceException(ErrorCode.USER_NOT_FOUND)
    class InvalidPasswordException : ServiceException(ErrorCode.USER_INVALID_PASSWORD)
    class StudentNoAlreadyExistsException : ServiceException(ErrorCode.STUDENT_NO_ALREADY_EXISTS)

}