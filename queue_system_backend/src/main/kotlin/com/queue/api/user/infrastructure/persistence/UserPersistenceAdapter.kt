package com.queue.api.user.infrastructure.persistence

import com.queue.api.user.application.port.out.UserPort
import com.queue.api.user.domain.User
import com.queue.global.common.enums.Role
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@Repository
class UserPersistenceAdapter : UserPort {
    private val users = ConcurrentHashMap<String, User>()
    private val idGenerator = AtomicLong(1)

    init {
        // 기본 어드민 계정 추가
        val admin = User(
            id = idGenerator.getAndIncrement(),
            studentNo = "admin",
            password = "admin",
            name = "관리자",
            role = Role.ADMIN
        )
        users[admin.studentNo] = admin
    }

    override fun save(user: User): User {
        val id = user.id ?: idGenerator.getAndIncrement()
        val savedUser = User(
            id = id,
            studentNo = user.studentNo,
            password = user.password,
            name = user.name,
            role = user.role
        )
        users[savedUser.studentNo] = savedUser
        return savedUser
    }

    override fun findByStudentNo(studentNo: String): User? {
        return users[studentNo]
    }

    override fun existsByStudentNo(studentNo: String): Boolean {
        return users.containsKey(studentNo)
    }
}
