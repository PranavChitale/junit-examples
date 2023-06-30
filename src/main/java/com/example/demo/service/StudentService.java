package com.example.demo.service;

import com.example.demo.constants.ResponseType;
import com.example.demo.dao.CourseEnrollmentRepository;
import com.example.demo.dao.StudentRepository;
import com.example.demo.entity.CourseEnrollment;
import com.example.demo.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseEnrollmentRepository courseEnrollmentRepository;

    @Autowired
    private CourseEnrollmentService courseEnrollmentService;

    @Value("${junit.example.officeHours.start}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime officeHoursStartTime;

    @Value("${junit.example.officeHours.end}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime officeHoursEndTime;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public String getCourseEnrollments(Long studentId) {
        String response;
        Student student = getStudentById(studentId);
        if (student != null) {
            int nCourses = student.getEnrolledCourses().size();
            if (nCourses > 0) {
                List<String> courseNames = new ArrayList<>();
                for (int i = 0; i < nCourses; i++) {
                    courseNames.add(student.getEnrolledCourses().get(i).getCourse().getName());
                }
                response = courseNames.toString();
            } else {
                response = ResponseType.NOT_ENROLLED.getMessage();
            }
        } else {
            response = ResponseType.INVALID_STUDENT.getMessage();
        }
        return response;
    }

    public String getEnrollmentAssistance(Long studentId) {
        LocalTime currentTime = LocalTime.now();
        Student student = getStudentById(studentId);
        String response;
        if (student != null) {
            if (currentTime.isAfter(officeHoursStartTime) && currentTime.isBefore(officeHoursEndTime)) {
                return ResponseType.CONTACT_REQ_RECV.getMessage();
            } else {
                return ResponseType.CONTACT_REQ_HOLD.getMessage() + student.getEmail();
            }
        } else {
            response = ResponseType.INVALID_STUDENT.getMessage();
        }
        return response;
    }

    public String enrollStudentInCourse(Long studentId, Long courseId) {
        String response;
        Student student = getStudentById(studentId);
        if (student != null) {
            response = courseEnrollmentService.enrollStudentInCourse((s, c) -> {
                CourseEnrollment enrollment = courseEnrollmentRepository.findByStudentIdAndCourseId(s.getId(), c.getId());
                if (enrollment == null) {
                    enrollment = new CourseEnrollment(s, c);
                    courseEnrollmentRepository.save(enrollment);
                    return ResponseType.SUCCESS.getMessage();
                } else {
                    return ResponseType.ALREADY_ENROLLED.getMessage();
                }
            }, student, courseId);
        } else {
            response = ResponseType.INVALID_STUDENT.getMessage();
        }
        return response;
    }

    private Student getStudentById(Long studentId) {
        Student student = null;
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        try {
            student = studentOptional.orElseThrow();
        } catch (Exception e) {
            System.out.println(e);
        }
        return student;
    }

}