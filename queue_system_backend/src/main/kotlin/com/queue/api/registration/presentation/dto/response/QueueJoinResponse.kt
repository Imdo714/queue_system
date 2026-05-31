package com.queue.api.registration.presentation.dto.response

data class QueueJoinResponse(
    val immediate: Boolean,           // true = 즉시 확정, false = 대기 중
    val rank: Long? = null,           // 1-based 순번 (대기 중일 때만)
    val totalInQueue: Long? = null    // 전체 대기 인원 (대기 중일 때만)
)
