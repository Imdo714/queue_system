package com.queue.api.registration.presentation.dto.response

import com.queue.api.registration.domain.Registration
import com.queue.global.common.enums.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

data class RegistrationResponse(
    val id: Long,
    val courseId: Long,
    val courseCode: String,
    val title: String,
    val dayOfWeek: DayOfWeek,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val registrationDate: LocalDateTime
) {
    companion object {
        fun from(registration: Registration): RegistrationResponse {
            return RegistrationResponse(
                id = registration.id!!,
                courseId = registration.course.id!!,
                courseCode = registration.course.courseCode,
                title = registration.course.title,
                dayOfWeek = registration.course.dayOfWeek,
                startTime = registration.course.startTime,
                endTime = registration.course.endTime,
                registrationDate = registration.registrationDate
            )
        }
    }
}
