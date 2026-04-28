package com.queue.api.user.application.port.out

import com.queue.api.user.domain.User

interface UserPort {
    fun save(user: User): User
    fun findByStudentNo(studentNo: String): User?
    fun existsByStudentNo(studentNo: String): Boolean
}
