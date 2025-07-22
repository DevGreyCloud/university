package com.acme.university.controllers;

import com.acme.university.dtos.StudentCreateDto;
import com.acme.university.dtos.StudentDto;
import com.acme.university.exceptions.LecturerNotFoundException;
import com.acme.university.exceptions.StudentAlreadyExistsException;
import com.acme.university.repositories.LecturerRepository;
import com.acme.university.repositories.StudentRepository;
import com.acme.university.services.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
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

    private StudentService mockStudentService;

    @org.junit.jupiter.api.BeforeEach
    public void setUp() {
        // Create a fresh mock for each test
        mockStudentService = Mockito.mock(StudentService.class);

        // Replace the autowired StudentService with our mock in the controller
        ReflectionTestUtils.setField(
            this.mockMvc.getDispatcherServlet().getWebApplicationContext()
                .getBean(StudentController.class), 
            "studentService", 
            mockStudentService
        );
    }

    @Test
    public void testCreateStudentWithoutLecturer() throws Exception {
        // Prepare test data with missing lecturerId
        StudentCreateDto studentDto = new StudentCreateDto();
        studentDto.setName("Alice");
        studentDto.setSurname("Johnson");
        // lecturerId is intentionally not set

        // We don't need to mock the service here since validation happens before service call
        // The controller should return a validation error

        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.lecturerId").exists()); // Error for missing lecturerId
    }

    @Test
    public void testCreateStudentWithExistingLecturer() throws Exception {
        // Prepare test data
        Long lecturerId = 1L;
        StudentCreateDto studentDto = new StudentCreateDto();
        studentDto.setName("Bob");
        studentDto.setSurname("Smith");
        studentDto.setLecturerId(lecturerId);

        // Create response DTO to be returned by the mocked service
        StudentDto createdStudentDto = new StudentDto();
        createdStudentDto.setId(1L);
        createdStudentDto.setName("Bob");
        createdStudentDto.setSurname("Smith");

        // Configure mock behavior
        Mockito.when(mockStudentService.createStudent(Mockito.any(StudentCreateDto.class)))
               .thenReturn(createdStudentDto);

        // Perform the request and verify results
        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Bob")))
                .andExpect(jsonPath("$.surname", is("Smith")));

        // Verify the service method was called with the correct arguments
        Mockito.verify(mockStudentService).createStudent(Mockito.argThat(dto -> 
            dto.getName().equals("Bob") && 
            dto.getSurname().equals("Smith") &&
            dto.getLecturerId().equals(lecturerId)
        ));
    }

    @Test
    public void testCreateDuplicateStudent() throws Exception {
        // Prepare test data
        Long lecturerId = 1L;
        StudentCreateDto studentDto = new StudentCreateDto();
        studentDto.setName("Charlie");
        studentDto.setSurname("Brown");
        studentDto.setLecturerId(lecturerId);

        // Configure mock to throw StudentAlreadyExistsException
        Mockito.when(mockStudentService.createStudent(Mockito.any(StudentCreateDto.class)))
               .thenThrow(new StudentAlreadyExistsException());

        // Perform the request and verify we get conflict response
        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isConflict());

        // Verify the service method was called with the correct arguments
        Mockito.verify(mockStudentService).createStudent(Mockito.argThat(dto -> 
            dto.getName().equals("Charlie") && 
            dto.getSurname().equals("Brown") &&
            dto.getLecturerId().equals(lecturerId)
        ));
    }

    @Test
    public void testCreateStudentWithNonExistentLecturer() throws Exception {
        // Prepare test data with non-existent lecturer ID
        Long nonExistentLecturerId = 999L;
        StudentCreateDto studentDto = new StudentCreateDto();
        studentDto.setName("David");
        studentDto.setSurname("Jones");
        studentDto.setLecturerId(nonExistentLecturerId);

        // Configure mock to throw LecturerNotFoundException
        Mockito.when(mockStudentService.createStudent(Mockito.any(StudentCreateDto.class)))
               .thenThrow(new LecturerNotFoundException());

        // Perform the request and verify we get not found response
        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isNotFound());

        // Verify the service method was called with the correct arguments
        Mockito.verify(mockStudentService).createStudent(Mockito.argThat(dto -> 
            dto.getName().equals("David") && 
            dto.getSurname().equals("Jones") &&
            dto.getLecturerId().equals(nonExistentLecturerId)
        ));
    }

    @Test
    public void testCreateStudentWithValidationErrors() throws Exception {
        // Create student with invalid data
        StudentCreateDto studentDto = new StudentCreateDto();
        studentDto.setName(""); // Empty name - should fail validation
        studentDto.setSurname("Wilson");
        studentDto.setLecturerId(1L); // Add lecturer ID

        // We don't need to mock the service here since validation happens before service call
        // The controller should return a validation error

        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").exists()); // Error for name field
    }
}