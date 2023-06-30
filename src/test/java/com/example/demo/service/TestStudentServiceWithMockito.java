package com.example.demo.service;

import com.example.demo.constants.ResponseType;
import com.example.demo.dao.CourseEnrollmentRepository;
import com.example.demo.dao.CourseRepository;
import com.example.demo.dao.StudentRepository;
import com.example.demo.entity.Course;
import com.example.demo.entity.CourseEnrollment;
import com.example.demo.entity.Student;
import com.example.demo.util.EnrollmentProcess;
import com.fasterxml.jackson.core.io.JsonEOFException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Method;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class TestStudentServiceWithMockito {

    @InjectMocks
    StudentService studentService;

    @Mock
    StudentRepository studentRepository;

    @Mock
    CourseEnrollmentService courseEnrollmentService;

    @Mock
    CourseEnrollmentRepository courseEnrollmentRepository;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(studentService, "officeHoursStartTime", LocalTime.of(9,0,0));
        ReflectionTestUtils.setField(studentService, "officeHoursEndTime", LocalTime.of(17,0,0));
    }

    @Test
    public void testGetAllStudents() {
        Student s1 = Mockito.mock(Student.class);
        Student s2 = Mockito.mock(Student.class);
        List<Student> students = List.of(s1,s2);

        Mockito.when(studentRepository.findAll()).thenReturn(students);

        List<Student> studentsReturned = studentService.getAllStudents();

        Assert.assertEquals(students.size(), studentsReturned.size());
        Mockito.verify(studentRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void testGetCourseEnrollments_ReturnsCourseEnrollments() {
        Long studentId = 1L;
        String course1Name = "testCourse1", course2Name = "testCourse2";
        Student s = Mockito.mock(Student.class, Mockito.RETURNS_DEEP_STUBS);

        Mockito.when(studentRepository.findById(studentId)).thenReturn(Optional.of(s));
        Mockito.when(s.getEnrolledCourses().size()).thenReturn(2);
        Mockito.when(s.getEnrolledCourses().get(0).getCourse().getName()).thenReturn("testCourse1");
        Mockito.when(s.getEnrolledCourses().get(1).getCourse().getName()).thenReturn("testCourse2");

        String response = studentService.getCourseEnrollments(studentId);

        Assert.assertEquals(List.of(course1Name,course2Name).toString(), response);
    }

    @Test(expected = NullPointerException.class)
    public void testGetCourseEnrollments_ReturnsException() {
        Long studentId = 1L;
        Student s = Mockito.mock(Student.class);

        Mockito.when(s.getEnrolledCourses()).thenReturn(null);
        Mockito.when(studentRepository.findById(studentId)).thenReturn(Optional.of(s));

        studentService.getCourseEnrollments(studentId);
    }

    @Test
    public void testPrivateGetStudentById_ReturnsStudent() throws Exception {
        Long studentId = 1L;
        Student s = Mockito.mock(Student.class);

        Mockito.when(studentRepository.findById(studentId)).thenReturn(Optional.of(s));

        Method getStudentByIdMethod = StudentService.class.getDeclaredMethod("getStudentById", Long.class);
        getStudentByIdMethod.setAccessible(true);
        Student sReturned = (Student) getStudentByIdMethod.invoke(studentService, studentId);

        Assert.assertEquals(s, sReturned);
        Mockito.verify(studentRepository, Mockito.times(1)).findById(studentId);
    }

    @Test
    public void testPrivateGetStudentById_ReturnsNull() throws Exception {
        Long studentId = 1L;

        Method getStudentByIdMethod = StudentService.class.getDeclaredMethod("getStudentById", Long.class);
        getStudentByIdMethod.setAccessible(true);
        Student sReturned = (Student) getStudentByIdMethod.invoke(studentService, studentId);

        Assert.assertNull(sReturned);
        Mockito.verify(studentRepository, Mockito.times(1)).findById(studentId);
    }

    @Test
    public void testEnrollStudentInCourse_WhenNotEnrolled_CreatesCourseEnrollment() {
        Long studentId = 1L, courseId = 2L;
        Student s = Mockito.mock(Student.class);
        Course c = Mockito.mock(Course.class);

        Mockito.when(s.getId()).thenReturn(studentId);
        Mockito.when(c.getId()).thenReturn(courseId);
        Mockito.when(studentRepository.findById(studentId)).thenReturn(Optional.of(s));
        Mockito.when(courseEnrollmentService.enrollStudentInCourse(Mockito.any(EnrollmentProcess.class), Mockito.eq(s), Mockito.eq(courseId))).thenAnswer(
                new Answer<>() {
                    @Override
                    public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                        return ((EnrollmentProcess) invocationOnMock.getArgument(0)).run(s, c);
                    }
                }
            // Can be simplified as follows
            // invocationOnMock -> ((EnrollmentProcess) invocationOnMock.getArgument(0)).run(s, c)
        );

        String response = studentService.enrollStudentInCourse(studentId, courseId);

        Assert.assertEquals(ResponseType.SUCCESS.getMessage(), response);
        Mockito.verify(courseEnrollmentRepository, Mockito.times(1)).findByStudentIdAndCourseId(studentId, courseId);
        Mockito.verify(courseEnrollmentRepository, Mockito.times(1)).save(Mockito.any(CourseEnrollment.class));
    }

    @Test
    public void testEnrollStudentInCourse_WhenAlreadyEnrolled_SkipsCourseEnrollment() {
        Long studentId = 1L, courseId = 2L;
        Student s = Mockito.mock(Student.class);
        Course c = Mockito.mock(Course.class);
        CourseEnrollment ce = Mockito.mock(CourseEnrollment.class);

        Mockito.when(s.getId()).thenReturn(studentId);
        Mockito.when(c.getId()).thenReturn(courseId);
        Mockito.when(studentRepository.findById(studentId)).thenReturn(Optional.of(s));
        Mockito.when(courseEnrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)).thenReturn(ce);
        Mockito.when(courseEnrollmentService.enrollStudentInCourse(Mockito.any(EnrollmentProcess.class), Mockito.eq(s), Mockito.eq(courseId))).thenAnswer(
            invocationOnMock -> ((EnrollmentProcess) invocationOnMock.getArgument(0)).run(s, c)
        );

        String response = studentService.enrollStudentInCourse(studentId, courseId);

        Assert.assertEquals(ResponseType.ALREADY_ENROLLED.getMessage(), response);
        Mockito.verify(courseEnrollmentRepository, Mockito.times(1)).findByStudentIdAndCourseId(studentId, courseId);
        Mockito.verify(courseEnrollmentRepository, Mockito.times(0)).save(Mockito.any(CourseEnrollment.class));
    }
}