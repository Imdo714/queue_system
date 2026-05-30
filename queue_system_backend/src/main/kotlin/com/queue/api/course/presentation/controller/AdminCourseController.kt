package com.queue.api.course.presentation.controller

import com.queue.api.course.application.port.`in`.CourseUseCase
import com.queue.api.course.presentation.dto.request.CreateCourseRequest
import com.queue.api.course.presentation.dto.request.DeleteCourseRequest
import com.queue.global.common.response.BaseResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/course")
class AdminCourseController(
    private val courseUseCase: CourseUseCase
) {

    @PostMapping
    fun createCourse(
        @RequestBody request: CreateCourseRequest
    ): BaseResponse<Unit> {
        courseUseCase.createCourse(request)
        return BaseResponse.ok()
    }

    @DeleteMapping("/{courseId}")
    fun deleteCourse(
        @PathVariable courseId: Long,
        @RequestBody request: DeleteCourseRequest
    ): BaseResponse<Unit> {
        courseUseCase.deleteCourse(request.studentNo, courseId)
        return BaseResponse.ok()
    }

}
