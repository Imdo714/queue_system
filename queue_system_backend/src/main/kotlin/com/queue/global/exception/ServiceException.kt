package com.queue.global.exception

import com.queue.global.exception.enums.ErrorCode

open class ServiceException (
    val errorCode: ErrorCode,
    cause: Throwable? = null
) : RuntimeException(errorCode.message, cause) {

    class UserNotFoundException : ServiceException(ErrorCode.USER_NOT_FOUND)
    class InvalidPasswordException : ServiceException(ErrorCode.USER_INVALID_PASSWORD)
    class StudentNoAlreadyExistsException : ServiceException(ErrorCode.STUDENT_NO_ALREADY_EXISTS)
    class UnauthorizedException : ServiceException(ErrorCode.UNAUTHORIZED)

    class CourseNotFoundException : ServiceException(ErrorCode.COURSE_NOT_FOUND)
    class CourseAlreadyExistsException : ServiceException(ErrorCode.COURSE_ALREADY_EXISTS)
    class CourseFullException : ServiceException(ErrorCode.COURSE_FULL)
    class CourseClosedException : ServiceException(ErrorCode.COURSE_CLOSED)

    class RegistrationAlreadyExistsException : ServiceException(ErrorCode.REGISTRATION_ALREADY_EXISTS)
    class RegistrationNotFoundException : ServiceException(ErrorCode.REGISTRATION_NOT_FOUND)
    class RegistrationTimeConflictException : ServiceException(ErrorCode.REGISTRATION_TIME_CONFLICT)
}