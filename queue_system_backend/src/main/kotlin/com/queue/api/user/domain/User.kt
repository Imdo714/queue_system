package com.queue.api.user.domain

import com.queue.global.common.enums.Role

class User(
    val id: Long? = null,
    val studentNo: String,
    val password: String,
    val name: String,
    var role: Role = Role.STUDENT
) {
    companion object {
        fun create(studentNo: String, password: String, name: String): User {
            return User(
                studentNo = studentNo,
                password = password,
                name = name,
                role = Role.STUDENT
            )
        }
    }
}
