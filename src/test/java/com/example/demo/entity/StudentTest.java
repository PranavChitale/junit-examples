package com.example.demo.entity;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class StudentTest {

    Student student = new Student();

    @Test
    public void testGetSetId() {
        Long id = 1L;

        student.setId(id);
        Long returnedId = student.getId();

        Assert.assertEquals(id, returnedId);
    }

    @Test
    public void testGetSetName() {
        String name = "John Doe";

        student.setName(name);
        String returnedName = student.getName();

        Assert.assertEquals(name, returnedName);
    }

    @Test
    public void testGetSetEmail() {
        String email = "john.doe@university.edu";

        student.setEmail(email);
        String returnedEmail = student.getEmail();

        Assert.assertEquals(email, returnedEmail);
    }

    @Test
    public void testGetSetDepartment() {
        String department = "CS";

        student.setDepartment(department);
        String returnedDepartment = student.getDepartment();

        Assert.assertEquals(department, returnedDepartment);
    }

    @Test
    public void testGetSetEnrolledCourses() {
        List<CourseEnrollment> enrolledCourses = new ArrayList<>();

        student.setEnrolledCourses(enrolledCourses);
        List<CourseEnrollment> returnedEnrolledCourses = student.getEnrolledCourses();

        Assert.assertEquals(enrolledCourses, returnedEnrolledCourses);
    }
}