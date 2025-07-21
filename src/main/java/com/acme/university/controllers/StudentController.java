package com.acme.university.controllers;

import com.acme.university.dtos.StudentDto;
import com.acme.university.dtos.StudentDto;
import com.acme.university.repositories.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentRepository studentRepository;

    @GetMapping
    public Iterable<StudentDto> getLecturers() {
        return studentRepository.findAll()
                .stream()
                .map(lecturer -> new StudentDto(lecturer.getId(), lecturer.getName(), lecturer.getSurname()))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getLecturerById(@PathVariable Long id) {
        var lecturer = studentRepository.findById(id).orElse(null);
        if (lecturer == null) {
            return ResponseEntity.notFound().build();
        }

        var StudentDto = new StudentDto(lecturer.getId(), lecturer.getName(), lecturer.getSurname());
        return ResponseEntity.ok(StudentDto);
    }
}
