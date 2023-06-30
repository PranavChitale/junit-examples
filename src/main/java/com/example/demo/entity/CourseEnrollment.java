package com.example.demo.entity;

import javax.persistence.*;

@Entity
@Table
public class CourseEnrollment {

    @Id
    @SequenceGenerator(
            name="enrollment_sequence",
            sequenceName = "enrollment_sequence",
            allocationSize = 1 // sequence increment
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "enrollment_sequence"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Student student;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Course course;

    public CourseEnrollment() {}

    public CourseEnrollment(Student student, Course course) {
        this.student = student;
        this.course = course;
    }

    public CourseEnrollment(Long id, Student student, Course course) {
        this.id = id;
        this.student = student;
        this.course = course;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}