package com.queue.api.course.presentation.dto.request

import com.queue.global.common.enums.DayOfWeek
import java.time.LocalTime

data class CreateCourseRequest(
    val studentNo: String,
    val courseCode: String,
    val title: String,
    val maxCapacity: Int,
    val dayOfWeek: DayOfWeek,
    val startTime: LocalTime,
    val endTime: LocalTime
)
