package com.example.demo.config;

import com.example.demo.dao.CourseEnrollmentRepository;
import com.example.demo.dao.CourseRepository;
import com.example.demo.dao.StudentRepository;
import com.example.demo.entity.Course;
import com.example.demo.entity.CourseEnrollment;
import com.example.demo.entity.Student;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ApplicationConfig {
    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository, CourseRepository courseRepository, CourseEnrollmentRepository courseEnrollmentRepository) {
        return args -> {
            Student s1 = new Student(
                    1L,
                    "John Doe",
                    "john.doe@university.edu",
                    "CS"
            );
            Student s2 = new Student(
                    2L,
                    "Jane Doe",
                    "jane.doe@university.edu",
                    "CS"
            );
            studentRepository.saveAll(List.of(s1,s2));

            Course c1 = new Course(
                    1L,
                    "Data Structures & Algorithms",
                    "CS",
                    true
            );
            Course c2 = new Course(
                    2L,
                    "Database Management Systems",
                    "CS",
                    true
            );
            courseRepository.saveAll(List.of(c1,c2));

            CourseEnrollment ce11 = new CourseEnrollment(
                    1L,
                    s1,
                    c1
            );
            CourseEnrollment ce12 = new CourseEnrollment(
                    2L,
                    s1,
                    c2
            );
            courseEnrollmentRepository.saveAll(List.of(ce11,ce12));
        };
    }
}