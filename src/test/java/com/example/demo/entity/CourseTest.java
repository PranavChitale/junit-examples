package com.example.demo.entity;

import org.junit.Assert;
import org.junit.Test;

public class CourseTest {

    Course course = new Course();

    @Test
    public void testAll() {
        Long id = 1L;
        String name = "Object Oriented Programming";
        String department = "CS";

        course.setId(id);
        course.setName(name);
        course.setDepartment(department);

        Assert.assertEquals(id, course.getId());
        Assert.assertEquals(name, course.getName());
        Assert.assertEquals(department, course.getDepartment());
    }

}