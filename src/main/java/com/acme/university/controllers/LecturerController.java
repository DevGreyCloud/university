package com.acme.university.controllers;

import com.acme.university.dtos.LecturerCreateDto;
import com.acme.university.dtos.LecturerDto;
import com.acme.university.dtos.LecturerSummaryDto;
import com.acme.university.exceptions.LecturerAlreadyExistsException;
import com.acme.university.exceptions.LecturerNotFoundException;
import com.acme.university.services.LecturerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/lecturers")
public class LecturerController {
    private final LecturerService lecturerService;

    @GetMapping
    public ResponseEntity<Iterable<LecturerSummaryDto>> getLecturers() {
        return ResponseEntity.ok(lecturerService.getLecturers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LecturerDto> getLecturerById(@PathVariable Long id) {
        var lecturer = lecturerService.getLecturerById(id);

        return ResponseEntity.ok(lecturer);
    }

    @PostMapping
    public ResponseEntity<?> createLecturer(
            @Valid @RequestBody LecturerCreateDto lecturerCreateDto,
            UriComponentsBuilder uriBuilder) {
        var lecturerDto = lecturerService.createLecturer(lecturerCreateDto);
        var uri = uriBuilder.path("/lecturers/{id}").buildAndExpand(lecturerDto.getId()).toUri();

        return ResponseEntity.created(uri).body(lecturerDto);
    }

    @ExceptionHandler(LecturerNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleLecturerNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Lecturer not found."));
    }

    @ExceptionHandler(LecturerAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleLecturerAlreadyExists() {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Lecturer already exists. Please try again with a different name and surname, or contact the administrator if you believe this is an error on our side."));
    }
}