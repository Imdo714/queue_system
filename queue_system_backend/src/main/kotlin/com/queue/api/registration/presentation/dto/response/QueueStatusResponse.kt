package com.queue.api.registration.presentation.dto.response

data class QueueStatusResponse(
    val status: QueueStatus,
    val rank: Long? = null,        // 1-based 순번, CONFIRMED 이면 null
    val totalInQueue: Long = 0
) {
    enum class QueueStatus { WAITING, CONFIRMED }
}
