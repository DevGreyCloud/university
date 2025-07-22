package com.acme.university.controllers;

import com.acme.university.dtos.LecturerCreateDto;
import com.acme.university.entities.Lecturer;
import com.acme.university.repositories.LecturerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
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
public class LecturerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Test
    public void testCreateLecturer() throws Exception {
        // Prepare test data
        LecturerCreateDto lecturerDto = new LecturerCreateDto();
        lecturerDto.setName("John");
        lecturerDto.setSurname("Doe");

        // Create lecturer and verify response
        mockMvc.perform(post("/lecturers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lecturerDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("John")))
                .andExpect(jsonPath("$.surname", is("Doe")));
    }

    @Test
    public void testCreateDuplicateLecturer() throws Exception {
        // First, save a lecturer
        Lecturer lecturer = Lecturer.builder()
                .name("Jane")
                .surname("Smith")
                .build();
        lecturerRepository.save(lecturer);

        // Attempt to create a duplicate
        LecturerCreateDto lecturerDto = new LecturerCreateDto();
        lecturerDto.setName("Jane");
        lecturerDto.setSurname("Smith");

        mockMvc.perform(post("/lecturers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lecturerDto)))
                .andExpect(status().isConflict());
    }

    @Test
    public void testGetLecturer() throws Exception {
        // Create lecturer in database
        Lecturer lecturer = Lecturer.builder()
                .name("Robert")
                .surname("Johnson")
                .build();
        lecturerRepository.save(lecturer);

        // Retrieve and verify
        mockMvc.perform(get("/lecturers/" + lecturer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Robert")))
                .andExpect(jsonPath("$.surname", is("Johnson")));
    }

    @Test
    public void testGetNonExistentLecturer() throws Exception {
        // Try to get a lecturer that doesn't exist
        mockMvc.perform(get("/lecturers/999"))
                .andExpect(status().isNotFound());
    }
}
