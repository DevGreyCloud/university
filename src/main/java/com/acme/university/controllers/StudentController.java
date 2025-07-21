package com.acme.university.controllers;

import com.acme.university.dtos.*;
import com.acme.university.entities.Lecturer;
import com.acme.university.entities.Student;
import com.acme.university.mappers.StudentMapper;
import com.acme.university.repositories.LecturerRepository;
import com.acme.university.repositories.StudentRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final LecturerRepository lecturerRepository;

    @GetMapping
    public Iterable<StudentDto> getStudents() {
        return studentRepository.findAll()
                .stream()
                .map(studentMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentNoIdDto> getStudentById(@PathVariable Long id) {
        var student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(studentMapper.toStudentNoIdDto(student));
    }

    @PostMapping
    public ResponseEntity<?> createStudent(
            @Valid @RequestBody StudentCreateDto studentCreateDto,
            UriComponentsBuilder uriBuilder) {
        Optional<Student> existingStudent = studentRepository.findByNameAndSurname(
                studentCreateDto.getName(),
                studentCreateDto.getSurname()
        );

        if (existingStudent.isPresent()) {
            ErrorResponse errorResponse = new ErrorResponse(
                    String.format("Student with name '%s' and surname '%s' already exists",
                            studentCreateDto.getName(),
                            studentCreateDto.getSurname())
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }

        Optional<Lecturer> existingLecturer = lecturerRepository.findById(
                studentCreateDto.getLecturerId()
        );

        if (existingLecturer.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse(
                    String.format("Lecturer with id '%d' does not exist",
                            studentCreateDto.getLecturerId())
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        var student = studentMapper.toEntity(studentCreateDto);
        student.addLecturer(existingLecturer.get());
        studentRepository.save(student);

        var studentDto = studentMapper.toDto(student);
        var uri = uriBuilder.path("/students/{id}").buildAndExpand(studentDto.getId()).toUri();

        var studentNoIdDto = studentMapper.toStudentNoIdDto(student);

        return ResponseEntity.created(uri).body(studentNoIdDto);
    }
}
