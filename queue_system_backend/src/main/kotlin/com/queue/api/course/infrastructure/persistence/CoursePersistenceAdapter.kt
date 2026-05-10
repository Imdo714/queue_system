package com.queue.api.course.infrastructure.persistence

import com.queue.api.course.application.port.out.CoursePort
import com.queue.api.course.domain.Course
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@Repository
class CoursePersistenceAdapter(

) : CoursePort {
    private val courses = ConcurrentHashMap<Long, Course>()
    private val courseCodes = ConcurrentHashMap<String, Long>()
    private val idGenerator = AtomicLong(1)

    override fun save(course: Course): Course {
        val id = course.id ?: idGenerator.getAndIncrement()
        val savedCourse = Course(
            id = id,
            courseCode = course.courseCode,
            title = course.title,
            maxCapacity = course.maxCapacity,
            currentEnrolled = course.currentEnrolled,
            status = course.status,
            dayOfWeek = course.dayOfWeek,
            startTime = course.startTime,
            endTime = course.endTime
        )
        courses[id] = savedCourse
        courseCodes[savedCourse.courseCode] = id
        return savedCourse
    }

    override fun findById(id: Long): Course? {
        return courses[id]
    }

    override fun findAll(): List<Course> {
        return courses.values.toList()
    }

    override fun findByCourseCode(courseCode: String): Course? {
        val id = courseCodes[courseCode] ?: return null
        return courses[id]
    }

    override fun existsByCourseCode(courseCode: String): Boolean {
        return courseCodes.containsKey(courseCode)
    }

    override fun delete(id: Long) {
        val course = courses.remove(id)
        if (course != null) {
            courseCodes.remove(course.courseCode)
        }
    }
}
