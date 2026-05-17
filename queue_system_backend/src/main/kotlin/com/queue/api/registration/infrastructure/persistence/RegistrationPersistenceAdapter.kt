package com.queue.api.registration.infrastructure.persistence

import com.queue.api.registration.application.port.out.RegistrationPort
import com.queue.api.registration.domain.Registration
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@Repository
class RegistrationPersistenceAdapter : RegistrationPort {

    private val registrations = ConcurrentHashMap<Long, Registration>()
    private val idGenerator = AtomicLong(1)

    override fun save(registration: Registration): Registration {
        val id = registration.id ?: idGenerator.getAndIncrement()
        val saved = Registration(
            id = id,
            user = registration.user,
            course = registration.course,
            registrationDate = registration.registrationDate
        )
        registrations[id] = saved
        return saved
    }

    override fun findById(id: Long): Registration? {
        return registrations[id]
    }

    override fun findByUserIdAndCourseId(userId: Long, courseId: Long): Registration? {
        return registrations.values.find {
            it.user.id == userId && it.course.id == courseId
        }
    }

    override fun findByUserId(userId: Long): List<Registration> {
        return registrations.values.filter { it.user.id == userId }
    }

    override fun delete(id: Long) {
        registrations.remove(id)
    }
}
