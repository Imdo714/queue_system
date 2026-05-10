package com.queue.api.course.presentation.dto.response

import com.queue.api.course.domain.Course
import com.queue.global.common.enums.CourseStatus
import com.queue.global.common.enums.DayOfWeek
import java.time.LocalTime

data class CourseResponse(
    val id: Long,
    val courseCode: String,
    val title: String,
    val maxCapacity: Int,
    val currentEnrolled: Int,
    val status: CourseStatus,
    val dayOfWeek: DayOfWeek,
    val startTime: LocalTime,
    val endTime: LocalTime
) {
    companion object {
        fun from(course: Course): CourseResponse {
            return CourseResponse(
                id = course.id!!,
                courseCode = course.courseCode,
                title = course.title,
                maxCapacity = course.maxCapacity,
                currentEnrolled = course.currentEnrolled,
                status = course.status,
                dayOfWeek = course.dayOfWeek,
                startTime = course.startTime,
                endTime = course.endTime
            )
        }
    }
}
