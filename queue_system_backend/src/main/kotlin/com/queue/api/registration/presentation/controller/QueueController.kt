package com.queue.api.registration.presentation.controller

import com.queue.api.registration.application.port.`in`.GetQueueStatusUseCase
import com.queue.api.registration.presentation.dto.response.QueueStatusResponse
import com.queue.global.common.response.BaseResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/registration/queue")
class QueueController(
    private val getQueueStatusUseCase: GetQueueStatusUseCase
) {

    /**
     * 대기 순번 폴링 엔드포인트.
     * useQueue.js 에서 1초마다 호출.
     *
     * GET /registration/queue/{courseId}/rank?studentNo=xxx
     *
     * 응답:
     *  - WAITING   : { status: "WAITING", rank: 5, totalInQueue: 30 }
     *  - CONFIRMED : { status: "CONFIRMED" }
     */
    @GetMapping("/{courseId}/rank")
    fun getQueueRank(
        @PathVariable courseId: Long,
        @RequestParam studentNo: String
    ): BaseResponse<QueueStatusResponse> {
        return BaseResponse.ok(getQueueStatusUseCase.getQueueStatus(studentNo, courseId))
    }
}
