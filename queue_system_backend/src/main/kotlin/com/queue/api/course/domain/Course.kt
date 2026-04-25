package com.queue.api.course.domain

import com.queue.global.common.enums.CourseStatus
import com.queue.global.common.enums.DayOfWeek
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalTime

@Entity
@Table(name = "courses")
class Course(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(nullable = false, unique = true)
    val courseCode: String,

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    val maxCapacity: Int,

    // 현재 신청 인원
    var currentEnrolled: Int = 0,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: CourseStatus = CourseStatus.OPEN,

    @Enumerated(EnumType.STRING) // 데이터베이스에 'MONDAY', 'TUESDAY' 형태로 저장됨
    @Column(nullable = false)
    val dayOfWeek: DayOfWeek,

    @Column(nullable = false)
    val startTime: LocalTime,

    @Column(nullable = false)
    val endTime: LocalTime
) {

}