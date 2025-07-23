package com.acme.university.controllers;

import com.acme.university.dtos.LecturerCreateDto;
import com.acme.university.dtos.LecturerDto;
import com.acme.university.dtos.LecturerSummaryDto;
import com.acme.university.exceptions.LecturerAlreadyExistsException;
import com.acme.university.exceptions.LecturerNotFoundException;
import com.acme.university.services.LecturerService;
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
@RequestMapping("/lecturers")
public class LecturerController {
    private static final Logger logger = LoggerFactory.getLogger(LecturerController.class);
    private final LecturerService lecturerService;

    @GetMapping
    @Operation(summary = "Get all lecturers")
    public ResponseEntity<Iterable<LecturerSummaryDto>> getLecturers() {
        logger.info("Received request to get all lecturers");

        var result = lecturerService.getLecturers();

        logger.debug("Returning {} lecturers", ((Collection<?>) result).size());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a lecturer by their id")
    public ResponseEntity<LecturerDto> getLecturerById(@PathVariable Long id) {
        logger.info("Received request to get lecturer by id: {}", id);

        var lecturer = lecturerService.getLecturerById(id);

        logger.debug("Lecturer found for id {}: {}", id, lecturer.getName());

        return ResponseEntity.ok(lecturer);
    }

    @PostMapping
    @Operation(summary = "Create a lecturer")
    public ResponseEntity<?> createLecturer(
            @Valid @RequestBody LecturerCreateDto lecturerCreateDto,
            UriComponentsBuilder uriBuilder) {
        logger.info("Received request to create lecturer: {} {}", lecturerCreateDto.getName(), lecturerCreateDto.getSurname());

        var lecturerDto = lecturerService.createLecturer(lecturerCreateDto);
        var uri = uriBuilder.path("/lecturers/{id}").buildAndExpand(lecturerDto.getId()).toUri();

        logger.info("Lecturer created with id: {}", lecturerDto.getId());

        return ResponseEntity.created(uri).body(lecturerDto);
    }

    @ExceptionHandler(LecturerNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleLecturerNotFound() {
        logger.warn("Lecturer not found exception handled");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Lecturer not found."));
    }

    @ExceptionHandler(LecturerAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleLecturerAlreadyExists() {
        logger.warn("Lecturer already exists exception handled");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Lecturer already exists. Please try again with a different name and surname, or contact the administrator if you believe this is an error on our side."));
    }
}