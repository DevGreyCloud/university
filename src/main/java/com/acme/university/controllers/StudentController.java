package com.acme.university.controllers;

import com.acme.university.dtos.*;
import com.acme.university.exceptions.LecturerNotFoundException;
import com.acme.university.exceptions.StudentAlreadyExistsException;
import com.acme.university.exceptions.StudentNotFoundException;
import com.acme.university.services.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/students")
public class StudentController {
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
    private final StudentService studentService;

    @GetMapping
    @Operation(summary = "Get all students")
    public ResponseEntity<Iterable<StudentSummaryDto>> getStudents() {
        logger.info("Received request to get all students");

        var result = studentService.getStudents();

        logger.debug("Returning {} students", ((Collection<?>) result).size());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a student by their id")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Long id) {
        logger.info("Received request to get student by id: {}", id);
        
        var student = studentService.getStudentById(id);
        
        logger.debug("Student found for id {}: {}", id, student.getName());
        
        return ResponseEntity.ok(student);
    }

    @PostMapping
    @Operation(summary = "Create a student")
    public ResponseEntity<?> createStudent(
            @Valid @RequestBody StudentCreateDto studentCreateDto,
            UriComponentsBuilder uriBuilder) {
        logger.info("Received request to create student: {} {}", studentCreateDto.getName(), 
        
        studentCreateDto.getSurname());
        var studentDto = studentService.createStudent(studentCreateDto);
        var uri = uriBuilder.path("/students/{id}").buildAndExpand(studentDto.getId()).toUri();
        
        logger.info("Student created with id: {}", studentDto.getId());
        
        return ResponseEntity.created(uri).body(studentDto);
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleStudentNotFound() {
        logger.warn("Student not found exception handled");
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Student not found."));
    }

    @ExceptionHandler(StudentAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleStudentAlreadyExists() {
        logger.warn("Student already exists exception handled");
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Student already exists. Please try again with a different name and surname, or contact the administrator if you believe this is an error on our side."));
    }

    @ExceptionHandler(LecturerNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleLecturerNotFound() {
        logger.warn("Lecturer not found exception handled");
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Lecturer not found."));
    }
}
