package com.queue.api.registration.infrastructure.persistence

import com.queue.api.registration.application.port.out.RegistrationCommandPort
import com.queue.api.registration.application.port.out.RegistrationQueryPort
import com.queue.api.registration.domain.Registration
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

/**
 * 인메모리 Outbound Adapter.
 *
 * RegistrationCommandPort (쓰기)와 RegistrationQueryPort (읽기) 를 함께 구현한다.
 * 현재는 ConcurrentHashMap 을 사용한 인메모리 저장소이며,
 * 추후 RDB 전환 시 이 클래스만 교체하면 된다 (포트-어댑터 패턴의 이점).
 */
@Repository
class RegistrationPersistenceAdapter : RegistrationCommandPort, RegistrationQueryPort {

    private val store = ConcurrentHashMap<Long, Registration>()
    private val idSequence = AtomicLong(1)

    // ── RegistrationCommandPort ───────────────────────────────────────────────

    override fun save(registration: Registration): Registration {
        val id = registration.id ?: idSequence.getAndIncrement()
        val saved = Registration(
            id = id,
            user = registration.user,
            course = registration.course,
            registrationDate = registration.registrationDate
        )
        store[id] = saved
        return saved
    }

    override fun delete(id: Long) {
        store.remove(id)
    }

    override fun deleteByCourseId(courseId: Long) {
        store.entries.removeIf { it.value.course.id == courseId }
    }

    // ── RegistrationQueryPort ─────────────────────────────────────────────────

    override fun findById(id: Long): Registration? = store[id]

    override fun findByUserIdAndCourseId(userId: Long, courseId: Long): Registration? =
        store.values.find { it.user.id == userId && it.course.id == courseId }

    override fun findByUserId(userId: Long): List<Registration> =
        store.values.filter { it.user.id == userId }
}
