package com.acme.university.controllers;

import com.acme.university.dtos.ErrorResponse;
import com.acme.university.dtos.LecturerDto;
import com.acme.university.dtos.LecturerNoIdDto;
import com.acme.university.dtos.LecturerSimplerDto;
import com.acme.university.entities.Lecturer;
import com.acme.university.mappers.LecturerMapper;
import com.acme.university.repositories.LecturerRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/lecturers")
public class LecturerController {
    private final LecturerRepository lecturerRepository;
    private final LecturerMapper lecturerMapper;

    @GetMapping
    public Iterable<LecturerDto> getLecturers() {
        return lecturerRepository.findAll()
                .stream()
                .map(lecturerMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LecturerNoIdDto> getLecturerById(@PathVariable Long id) {
        var lecturer = lecturerRepository.findById(id).orElse(null);
        if (lecturer == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(lecturerMapper.toLecturerNoIdDto(lecturer));
    }

    @PostMapping
    public ResponseEntity<?> createLecturer(
            @Valid @RequestBody LecturerSimplerDto lecturerSimplerDto,
            UriComponentsBuilder uriBuilder) {
        Optional<Lecturer> existingLecturer = lecturerRepository.findByNameAndSurname(
                lecturerSimplerDto.getName(), 
                lecturerSimplerDto.getSurname()
        );
        
        if (existingLecturer.isPresent()) {
            ErrorResponse errorResponse = new ErrorResponse(
                    String.format("Lecturer with name '%s' and surname '%s' already exists",
                            lecturerSimplerDto.getName(),
                            lecturerSimplerDto.getSurname())
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);

        }
        
        var lecturer = lecturerMapper.toEntity(lecturerSimplerDto);
        lecturerRepository.save(lecturer);

        var lecturerDto = lecturerMapper.toDto(lecturer);
        var uri = uriBuilder.path("/lecturers/{id}").buildAndExpand(lecturerDto.getId()).toUri();

        var lecturerNoIdDto = lecturerMapper.toLecturerNoIdDto(lecturer);

        return ResponseEntity.created(uri).body(lecturerNoIdDto);
    }
}