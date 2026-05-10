package com.queue.api.course.presentation.controller

import com.queue.api.course.application.port.`in`.GetCourseUseCase
import com.queue.api.course.presentation.dto.response.CourseResponse
import com.queue.global.common.response.BaseResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/course")
class CourseController(
    private val getCourseUseCase: GetCourseUseCase
) {

    @GetMapping
    fun getCourses(): BaseResponse<List<CourseResponse>> {
        return BaseResponse.ok(getCourseUseCase.getCourses())
    }

    @GetMapping("/{courseId}")
    fun getCourse(@PathVariable courseId: Long): BaseResponse<CourseResponse> {
        return BaseResponse.ok(getCourseUseCase.getCourse(courseId))
    }

}
