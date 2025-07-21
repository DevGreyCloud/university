package com.acme.university.controllers;

import com.acme.university.dtos.LecturerDto;
import com.acme.university.entities.Lecturer;
import com.acme.university.repositories.LecturerRepository;
import com.acme.university.services.LecturerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/lecturers")
public class LecturerController {
    private final LecturerRepository lecturerRepository;

    @GetMapping
    public Iterable<LecturerDto> getLecturers() {
        return lecturerRepository.findAll()
                .stream()
                .map(lecturer -> new LecturerDto(lecturer.getId(), lecturer.getName(), lecturer.getSurname()))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LecturerDto> getLecturerById(@PathVariable Long id) {
        var lecturer = lecturerRepository.findById(id).orElse(null);
        if (lecturer == null) {
            return ResponseEntity.notFound().build();
        }

        var lecturerDto = new LecturerDto(lecturer.getId(), lecturer.getName(), lecturer.getSurname());
        return ResponseEntity.ok(lecturerDto);
    }
}
