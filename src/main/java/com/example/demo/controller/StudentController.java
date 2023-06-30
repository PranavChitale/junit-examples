package com.example.demo.controller;

import com.example.demo.entity.Student;
import com.example.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/student/{studentId}/enrollments")
    public String getCourseEnrollments(@PathVariable("studentId") Long studentId) {
        return studentService.getCourseEnrollments(studentId);
    }

    @GetMapping("/student/{studentId}/contact")
    public String getEnrollmentAssistance(@PathVariable("studentId") Long studentId) {
        return studentService.getEnrollmentAssistance(studentId);
    }

    @PostMapping("/student/{studentId}/enroll/{courseId}")
    public String enrollStudentInCourse(@PathVariable("studentId") Long studentId, @PathVariable("courseId") Long courseId) {
        return studentService.enrollStudentInCourse(studentId, courseId);
    }
}