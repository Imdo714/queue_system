package com.queue.api.registration.application.port.`in`

import com.queue.api.registration.presentation.dto.response.QueueStatusResponse

interface GetQueueStatusUseCase {
    fun getQueueStatus(studentNo: String, courseId: Long): QueueStatusResponse
}
