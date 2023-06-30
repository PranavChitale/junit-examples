package com.example.demo.dao;

import com.example.demo.entity.Course;
import com.example.demo.entity.CourseEnrollment;
import com.example.demo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollment, Long> {

    @Query("SELECT ce FROM CourseEnrollment ce WHERE ce.student.id=?1 AND ce.course.id=?2")
    CourseEnrollment findByStudentIdAndCourseId(Long student_id, Long course_id);
}