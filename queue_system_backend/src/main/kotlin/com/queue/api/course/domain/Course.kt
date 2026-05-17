package com.queue.api.course.domain

import com.queue.api.registration.domain.Registration
import com.queue.global.common.enums.CourseStatus
import com.queue.global.common.enums.DayOfWeek
import jakarta.persistence.*
import java.time.LocalTime

@Entity
@Table(name = "courses")
class Course(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true, nullable = false)
    val courseCode: String,

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    val maxCapacity: Int,

    @Column(nullable = false)
    var currentEnrolled: Int = 0,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: CourseStatus = CourseStatus.OPEN,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val dayOfWeek: DayOfWeek,

    @Column(nullable = false)
    val startTime: LocalTime,

    @Column(nullable = false)
    val endTime: LocalTime,

    @OneToMany(mappedBy = "course", cascade = [CascadeType.ALL], orphanRemoval = true)
    val registrations: MutableList<Registration> = mutableListOf()

    ) {
        companion object {
            fun create(
                courseCode: String,
                title: String,
                maxCapacity: Int,
                dayOfWeek: DayOfWeek,
                startTime: LocalTime,
                endTime: LocalTime
            ): Course {
                return Course(
                    courseCode = courseCode,
                    title = title,
                    maxCapacity = maxCapacity,
                    dayOfWeek = dayOfWeek,
                    startTime = startTime,
                    endTime = endTime
                )
            }
        }
    }
