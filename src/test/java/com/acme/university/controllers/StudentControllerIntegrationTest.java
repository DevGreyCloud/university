package com.acme.university.controllers;

import com.acme.university.dtos.StudentCreateDto;
import com.acme.university.entities.Lecturer;
import com.acme.university.entities.Student;
import com.acme.university.repositories.LecturerRepository;
import com.acme.university.repositories.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
public class StudentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Test
    public void testCreateStudentWithoutLecturer() throws Exception {
        StudentCreateDto studentDto = new StudentCreateDto();
        studentDto.setName("Alice");
        studentDto.setSurname("Johnson");

        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.lecturerId").exists()); // Error for missing lecturerId
    }

    @Test
    public void testCreateStudentWithExistingLecturer() throws Exception {
        // First, create a lecturer
        Lecturer lecturer = Lecturer.builder()
                .name("Professor")
                .surname("Xavier")
                .build();
        lecturerRepository.save(lecturer);

        // Create student with lecturer assignment
        StudentCreateDto studentDto = new StudentCreateDto();
        studentDto.setName("Bob");
        studentDto.setSurname("Smith");
        studentDto.setLecturerId(lecturer.getId());

        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Bob")))
                .andExpect(jsonPath("$.surname", is("Smith")));

        // Verify the student was assigned to the lecturer
        Lecturer updatedLecturer = lecturerRepository.findById(lecturer.getId()).orElseThrow();
        assertEquals(1, updatedLecturer.getStudents().size());
        Student student = updatedLecturer.getStudents().iterator().next();
        assertEquals("Bob", student.getName());
        assertEquals("Smith", student.getSurname());
    }

    @Test
    public void testCreateDuplicateStudent() throws Exception {
        // First, create a lecturer
        Lecturer lecturer = Lecturer.builder()
                .name("Test")
                .surname("Professor")
                .build();
        lecturerRepository.save(lecturer);
        
        // First, save a student
        Student student = Student.builder()
                .name("Charlie")
                .surname("Brown")
                .build();
        studentRepository.save(student);

        // Attempt to create a duplicate
        StudentCreateDto studentDto = new StudentCreateDto();
        studentDto.setName("Charlie");
        studentDto.setSurname("Brown");
        studentDto.setLecturerId(lecturer.getId()); // Set lecturer ID

        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isConflict());
    }

    @Test
    public void testCreateStudentWithNonExistentLecturer() throws Exception {
        // Create student with non-existent lecturer ID
        StudentCreateDto studentDto = new StudentCreateDto();
        studentDto.setName("David");
        studentDto.setSurname("Jones");
        studentDto.setLecturerId(999L); // Non-existent ID

        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateStudentWithValidationErrors() throws Exception {
        // Create student with invalid data
        StudentCreateDto studentDto = new StudentCreateDto();
        studentDto.setName(""); // Empty name - should fail validation
        studentDto.setSurname("Wilson");

        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").exists()); // Error for name field
    }
}