package com.dk.unitandintegrationtesting.controllers;

import com.dk.unitandintegrationtesting.entities.Course;
import com.dk.unitandintegrationtesting.services.StudentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @DisplayName("retrieveDetailsForCourse Unit test")
    @Test
    void retrieveCoursesForStudent() throws Exception {
      List<Course> mockedCourseList= Arrays.asList(
                new Course("TestCourse1","Spring","Learnign Unit tests",
                Arrays.asList("Lear Spring Boot","Learn Unit Tests")),
                new Course("Course2","Spring2","Learning Spring Boot",
                        Arrays.asList("Doing some project","Go advanced"))
        );
        Mockito.when(studentService.retrieveCourses(Mockito.anyString()))
                .thenReturn(mockedCourseList);

        RequestBuilder requestBuilder=MockMvcRequestBuilders
                .get("/students/David/courses")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result=mockMvc.perform(requestBuilder).andReturn();

        Mockito.verify(studentService).retrieveCourses("David");
        String expected = "" +
                "[" +
                "{" +
                "\"id\":\"TestCourse1\"," +
                "\"name\":\"Spring\"," +
                "\"description\":\"Learnign Unit tests\"," +
                "\"steps\":" +
                "[" +
                "\"Lear Spring Boot\"," +
                "\"Learn Unit Tests\"" +
                "]" +
                "}," +
                "{" +
                "\"id\":\"Course2\"," +
                "\"name\":\"Spring2\"," +
                "\"description\":\"Learning Spring Boot\"," +
                "\"steps\":" +
                "[" +
                "\"Doing some project\"," +
                "\"Go advanced\"" +
                "]" +
                "}" +
                "]";
        JSONAssert.assertEquals(expected,result.getResponse().getContentAsString(),false);
    }

    @DisplayName("retrieveDetailsForCourse Unit test")
    @Test
    void retrieveDetailsForCourse() throws Exception {
        Course mockCourse = new Course("TestCourse", "Spring", "Unit testing",
                Arrays.asList("Learn Spring Boot", "Learn Unit Testing", "Learn Integration Testing"));
        Mockito.when(studentService.retrieveCourse(Mockito.anyString(),Mockito.anyString()))
                .thenReturn(mockCourse);

        RequestBuilder requestBuilder= MockMvcRequestBuilders
                .get("/students/David/courses/TestCourse")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result=mockMvc.perform(requestBuilder).andReturn();

        Mockito.verify(studentService).retrieveCourse("David","TestCourse");
        String expected = "{\"id\":\"TestCourse\",\"name\":\"Spring\",\"description\":\"Unit testing\",\"steps\":[\"Learn Spring Boot\",\"Learn Unit Testing\",\"Learn Integration Testing\"]}";
        JSONAssert.assertEquals(expected,result.getResponse().getContentAsString(),false);
    }

    @DisplayName("registerStudentForCourse Unit test")
    @Test
    void registerStudentForCourse() throws Exception {
        Course mockCourse=new Course("1","Smallest Number","1",Arrays.asList("1","2","3","4"));
        Mockito.when(studentService.addCourse(Mockito.anyString(),Mockito.any(Course.class)))
                .thenReturn(mockCourse);
        String exampleCourseJson = "" +
                "{" +
                "\"name\":\"Spring\"," +
                "\"description\":\"Unit testing\"," +
                "\"steps\":" +
                "[" +
                "\"Learn Spring Boot\"," +
                "\"Learn Unit Testing\"," +
                "\"Learn Integration Testing\"" +
                "]" +
                "}";
        RequestBuilder requestBuilder=MockMvcRequestBuilders
                .post("/students/Student1/courses")
                .accept(MediaType.APPLICATION_JSON)
                .content(exampleCourseJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result=mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response=result.getResponse();

        assertEquals(HttpStatus.CREATED.value(),response.getStatus());
        assertEquals("http://localhost/students/Student1/courses/1",response.getHeader(HttpHeaders.LOCATION));
    }
}