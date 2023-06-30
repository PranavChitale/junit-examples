package com.example.demo.util;

import com.example.demo.constants.ResponseType;
import com.example.demo.entity.Course;
import com.example.demo.entity.Student;

public interface EnrollmentProcess {
    String run(Student student, Course course);
}