package com.acme.university.controllers;

import com.acme.university.dtos.LecturerCreateDto;
import com.acme.university.dtos.StudentCreateDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;DB_CLOSE_DELAY=-1",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.flyway.enabled=false"
})
@AutoConfigureMockMvc
@Transactional
public class ErrorHandlingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testValidationErrorForLecturerCreation() throws Exception {
        // Create lecturer with invalid data
        LecturerCreateDto lecturerDto = new LecturerCreateDto();
        lecturerDto.setName(""); // Empty name - should fail validation
        lecturerDto.setSurname("Smith");

        mockMvc.perform(post("/lecturers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lecturerDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").exists()); // Error for name field
    }

    @Test
    public void testValidationErrorForStudentCreation() throws Exception {
        // Create student with invalid surname (special characters)
        StudentCreateDto studentDto = new StudentCreateDto();
        studentDto.setName("Valid");
        studentDto.setSurname("Invalid@Name"); // Contains special character

        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.surname").exists()); // Error for surname field
    }

    @Test
    public void testResourceNotFoundError() throws Exception {
        // Try to fetch a non-existent lecturer
        mockMvc.perform(get("/lecturers/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testMalformedJsonRequest() throws Exception {
        // Send malformed JSON
        String malformedJson = "{\"name\":\"John\", \"surname\":}"; // Missing value for surname

        mockMvc.perform(post("/lecturers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(malformedJson))
                .andExpect(status().isBadRequest());
    }
}