package com.dk.unitandintegrationtesting.controllers;

import com.dk.unitandintegrationtesting.SpringBootUnitAndIntegrationTestingApplication;
import com.dk.unitandintegrationtesting.entities.Course;
import com.dk.unitandintegrationtesting.services.StudentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(
        classes = SpringBootUnitAndIntegrationTestingApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class StudentControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    //    @Sql({ "schema.sql", "data.sql" })
    @DisplayName("RetrieveStudents Integration test")
    @Test
    void testRetrieveStudents() throws Exception {

        ResponseEntity<String> responseEntity = testRestTemplate
                .getForEntity("http://localhost:" + port + "/students/Student1/courses/Course1", String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        String expected = "{\"id\":\"Course1\",\"name\":\"Spring\",\"description\":\"10 Steps\",\"steps\":[\"Learn Maven\",\"Import Project\",\"First Example\",\"Second Example\"]}";
        JSONAssert.assertEquals(expected,responseEntity.getBody(),false);
    }


    @DisplayName("registerStudentForCourse Integration test")
    @Test
    void registerStudentForCourse() throws Exception {
        Course course = new Course("TestCourse22", "Spring", "Unit testing",
                Arrays.asList("Learn Spring Boot", "Learn Unit Testing", "Learn Integration Testing"));

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Course> entity = new HttpEntity<Course>(course, headers);
        ResponseEntity<String> responseEntity = testRestTemplate
                .postForEntity("http://localhost:" + port + "/students/Student1/courses", entity, String.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

}