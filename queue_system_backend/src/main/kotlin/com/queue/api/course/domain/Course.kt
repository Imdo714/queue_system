package com.queue.api.course.domain

import com.queue.global.common.enums.CourseStatus
import com.queue.global.common.enums.DayOfWeek
import java.time.LocalTime

class Course(
    val id: Long? = null,
    val courseCode: String,
    val title: String,
    val maxCapacity: Int,
    var currentEnrolled: Int = 0,
    var status: CourseStatus = CourseStatus.OPEN,
    val dayOfWeek: DayOfWeek,
    val startTime: LocalTime,
    val endTime: LocalTime
) {
    fun update(
        title: String? = null,
        maxCapacity: Int? = null,
        dayOfWeek: DayOfWeek? = null,
        startTime: LocalTime? = null,
        endTime: LocalTime? = null
    ): Course = Course(
        id = this.id,
        courseCode = this.courseCode,
        title = title ?: this.title,
        maxCapacity = maxCapacity ?: this.maxCapacity,
        currentEnrolled = this.currentEnrolled,
        status = this.status,
        dayOfWeek = dayOfWeek ?: this.dayOfWeek,
        startTime = startTime ?: this.startTime,
        endTime = endTime ?: this.endTime
    )

    fun increaseEnrolled() {
        currentEnrolled++
    }

    fun decreaseEnrolled() {
        currentEnrolled--
    }

    companion object {
        fun create(
            courseCode: String,
            title: String,
            maxCapacity: Int,
            dayOfWeek: DayOfWeek,
            startTime: LocalTime,
            endTime: LocalTime
        ) : Course = Course(
            courseCode = courseCode,
            title = title,
            maxCapacity = maxCapacity,
            dayOfWeek = dayOfWeek,
            startTime = startTime,
            endTime = endTime
        )
    }
}
