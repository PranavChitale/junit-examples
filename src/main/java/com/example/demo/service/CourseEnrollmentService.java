package com.example.demo.service;

import com.example.demo.constants.ResponseType;
import com.example.demo.dao.CourseRepository;
import com.example.demo.entity.Course;
import com.example.demo.entity.Student;
import com.example.demo.util.EnrollmentProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourseEnrollmentService {

    @Autowired
    private CourseRepository courseRepository;

    public String enrollStudentInCourse(EnrollmentProcess enrollmentProcess, Student student, Long courseId) {
        String response;
        Course course = getCourseById(courseId);
        if (course != null) {
            if (student.getDepartment().equals(course.getDepartment())) {
                response = enrollmentProcess.run(student, course);
            } else {
                response = ResponseType.FAIL_DEPT_MISMATCH.getMessage();
            }
        } else {
            response = ResponseType.INVALID_COURSE.getMessage();
        }
        return response;
    }

    private Course getCourseById(Long courseId) {
        Course course = null;
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        try {
            course = courseOptional.orElseThrow();
        } catch (Exception e) {
            System.out.println(e);
        }
        return course;
    }

}