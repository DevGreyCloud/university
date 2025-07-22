package com.acme.university.controllers;

import com.acme.university.dtos.*;
import com.acme.university.entities.Lecturer;
import com.acme.university.entities.Student;
import com.acme.university.exceptions.LecturerAlreadyExistsException;
import com.acme.university.exceptions.LecturerNotFoundException;
import com.acme.university.exceptions.StudentAlreadyExistsException;
import com.acme.university.exceptions.StudentNotFoundException;
import com.acme.university.services.StudentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    public Iterable<StudentDto> getStudents() {
        return studentService.getStudents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Long id) {
        var student = studentService.getStudentById(id);

        return ResponseEntity.ok(student);
    }

    @PostMapping
    public ResponseEntity<?> createStudent(
            @Valid @RequestBody StudentCreateDto studentCreateDto,
            UriComponentsBuilder uriBuilder) {
        var student = studentService.createStudent(studentCreateDto);
        var uri = uriBuilder.path("/students/{id}").buildAndExpand(student.getId()).toUri();

        var studentResponseDto = studentService.createStudentResponseDto(student);
        return ResponseEntity.created(uri).body(studentResponseDto);
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleStudentNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Student not found."));
    }

    @ExceptionHandler(StudentAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleStudentAlreadyExists() {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Student already exists. Please try again with a different name and surname, or contact the administrator if you believe this is an error on our side."));
    }

    @ExceptionHandler(LecturerNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleLecturerNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Lecturer not found."));
    }
}
