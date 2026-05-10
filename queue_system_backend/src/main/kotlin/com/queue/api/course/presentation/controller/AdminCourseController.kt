package com.queue.api.course.presentation.controller

import com.queue.api.course.application.port.`in`.CreateCourseUseCase
import com.queue.api.course.application.port.`in`.DeleteCourseUseCase
import com.queue.api.course.presentation.dto.request.CreateCourseRequest
import com.queue.api.course.presentation.dto.request.DeleteCourseRequest
import com.queue.global.common.response.BaseResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/course")
class AdminCourseController(
    private val createCourseUseCase: CreateCourseUseCase,
    private val deleteCourseUseCase: DeleteCourseUseCase
) {

    @PostMapping
    fun createCourse(
        @RequestBody request: CreateCourseRequest
    ): BaseResponse<Unit> {
        createCourseUseCase.createCourse(request)
        return BaseResponse.ok()
    }

    @DeleteMapping("/{courseId}")
    fun deleteCourse(
        @PathVariable courseId: Long,
        @RequestBody request: DeleteCourseRequest
    ): BaseResponse<Unit> {
        deleteCourseUseCase.deleteCourse(request.studentNo, courseId)
        return BaseResponse.ok()
    }

}
