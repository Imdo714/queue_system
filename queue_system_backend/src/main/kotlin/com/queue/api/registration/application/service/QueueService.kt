package com.queue.api.registration.application.service

import com.queue.api.registration.application.port.`in`.GetQueueStatusUseCase
import com.queue.api.registration.application.port.out.RegistrationRedisPort
import com.queue.api.registration.presentation.dto.response.QueueStatusResponse
import com.queue.api.registration.presentation.dto.response.QueueStatusResponse.QueueStatus
import org.springframework.stereotype.Service

@Service
class QueueService(
    private val registrationRedisPort: RegistrationRedisPort,
    private val registrationValidator: RegistrationValidator
) : GetQueueStatusUseCase {

    /**
     * 신청 순번 조회.
     * Sorted Set에 있으면 신청됨 + 순번 반환, 없으면 미신청.
     */
    override fun getQueueStatus(studentNo: String, courseId: Long): QueueStatusResponse {
        registrationValidator.validateAndGetUser(studentNo)
        registrationValidator.validateAndGetCourse(courseId)

        val rank = registrationRedisPort.getQueueRank(courseId, studentNo)
            ?: return QueueStatusResponse(status = QueueStatus.CONFIRMED)

        val total = registrationRedisPort.getQueueSize(courseId)
        return QueueStatusResponse(
            status = QueueStatus.CONFIRMED,
            rank = rank + 1,
            totalInQueue = total
        )
    }
}
