package com.queue.global.exception.enums

enum class ErrorCode(
    val httpStatus: Int,
    val code: String,
    val message: String
) {

    /** Global (공통) */
    INTERNAL_SERVER_ERROR(500, "G002", "서버 내부 오류가 발생했습니다."),

    /** User */
    USER_NOT_FOUND(404, "USER_NOT_FOUND", "존재하지 않는 사용자입니다."),
    USER_INVALID_PASSWORD(401, "USER_INVALID_PASSWORD", "유효하지 않은 비밀번호 입니다."),
    STUDENT_NO_ALREADY_EXISTS(409, "STUDENT_NO_ALREADY_EXISTS", "이미 가입되어있는 학번입니다."),

}