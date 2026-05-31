package com.queue.api.course.presentation.dto.request

import com.queue.global.common.enums.DayOfWeek
import java.time.LocalTime

data class CourseUpdateRequest(
    val studentNo: String,
    val title: String? = null,
    val maxCapacity: Int? = null,
    val dayOfWeek: DayOfWeek? = null,
    val startTime: LocalTime? = null,
    val endTime: LocalTime? = null
)
