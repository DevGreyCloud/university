package com.acme.university.controllers;

import com.acme.university.dtos.LecturerDto;
import com.acme.university.mappers.LecturerMapper;
import com.acme.university.repositories.LecturerRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<LecturerDto> getLecturerById(@PathVariable Long id) {
        var lecturer = lecturerRepository.findById(id).orElse(null);
        if (lecturer == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(lecturerMapper.toDto(lecturer));
    }
}
