package com.queue.api.user.domain

import com.queue.api.registration.domain.Registration
import com.queue.global.common.enums.Role
import jakarta.persistence.*

@Entity
@Table(name = "users")
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true, nullable = false)
    val studentNo: String,

    @Column(nullable = false)
    val password: String,

    @Column(nullable = false)
    val name: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: Role = Role.STUDENT,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val registrations: MutableList<Registration> = mutableListOf()
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
