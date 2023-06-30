package com.example.demo.service;

import com.example.demo.constants.ResponseType;
import com.example.demo.dao.StudentRepository;
import com.example.demo.entity.Student;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.Optional;

@RunWith(PowerMockRunner.class)
@PrepareForTest({StudentService.class})
public class TestStudentServiceWithPowerMockito {

    @InjectMocks
    StudentService studentService;

    @Mock
    StudentRepository studentRepository;


    @Before
    public void setup() {
        ReflectionTestUtils.setField(studentService, "officeHoursStartTime", LocalTime.of(9,0,0));
        ReflectionTestUtils.setField(studentService, "officeHoursEndTime", LocalTime.of(17,0,0));
    }

    @Test
    public void testRequestEnrollmentAssistance_DuringOfficeHours() {
        Long studentId = 1L;
        Student student = Mockito.mock(Student.class);

        Mockito.when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        LocalTime currentTime = LocalTime.of(12,0,0);
        PowerMockito.mockStatic(LocalTime.class);
        Mockito.when(LocalTime.now()).thenReturn(currentTime);

        String response = studentService.getEnrollmentAssistance(studentId);
        Assert.assertEquals(ResponseType.CONTACT_REQ_RECV.getMessage(), response);
    }

    @Test
    public void testRequestEnrollmentAssistance_OutsideOfficeHours() {
        Long studentId = 1L;
        String email = "john.doe@university.edu";
        Student student = Mockito.mock(Student.class);

        Mockito.when(student.getEmail()).thenReturn(email);
        Mockito.when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        LocalTime currentTime = LocalTime.of(0,0,0);
        PowerMockito.mockStatic(LocalTime.class);
        Mockito.when(LocalTime.now()).thenReturn(currentTime);

        String response = studentService.getEnrollmentAssistance(studentId);
        Assert.assertEquals(ResponseType.CONTACT_REQ_HOLD.getMessage() + email, response);
    }

    @Test
    public void testContactStaff_InvalidStudentId() {
        Long studentId = 1L;

        Mockito.when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        LocalTime currentTime = LocalTime.of(12,0,0);
        PowerMockito.mockStatic(LocalTime.class);
        Mockito.when(LocalTime.now()).thenReturn(currentTime);

        String response = studentService.getEnrollmentAssistance(studentId);
        Assert.assertEquals(ResponseType.INVALID_STUDENT.getMessage(), response);
    }

}