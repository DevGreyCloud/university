package com.acme.university.services;

import com.acme.university.dtos.LecturerCreateDto;
import com.acme.university.dtos.LecturerDto;
import com.acme.university.dtos.LecturerSummaryDto;
import com.acme.university.entities.Lecturer;
import com.acme.university.exceptions.LecturerAlreadyExistsException;
import com.acme.university.exceptions.LecturerNotFoundException;
import com.acme.university.mappers.LecturerMapper;
import com.acme.university.repositories.LecturerRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class LecturerService {
    private final LecturerRepository lecturerRepository;
    private final LecturerMapper lecturerMapper;

    @Cacheable(value = "lecturers")
    public Iterable<LecturerSummaryDto> getLecturers() {
        return lecturerRepository.findAll()
                .stream()
                .map(lecturerMapper::toSummaryDto)
                .toList();
    }

    @Cacheable(value = "lecturerById", key = "#id")
    public LecturerDto getLecturerById(Long id) {
        var lecturer = lecturerRepository.findById(id).orElse(null);
        if (lecturer == null) {
            throw new LecturerNotFoundException();
        }
        return lecturerMapper.toDto(lecturer);
    }

    public LecturerDto createLecturer(LecturerCreateDto lecturerCreateDto) {
        Optional<Lecturer> existingLecturer = lecturerRepository.findByNameAndSurname(
                lecturerCreateDto.getName(),
                lecturerCreateDto.getSurname()
        );

        if (existingLecturer.isPresent()) {
            throw new LecturerAlreadyExistsException();
        }

        var lecturer = lecturerMapper.toEntity(lecturerCreateDto);
        lecturerRepository.save(lecturer);

        return lecturerMapper.toDto(lecturer);
    }
}
