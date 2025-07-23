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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@AllArgsConstructor
@Service
public class LecturerService {
    private static final Logger logger = LoggerFactory.getLogger(LecturerService.class);
    private final LecturerRepository lecturerRepository;
    private final LecturerMapper lecturerMapper;

    @Cacheable(value = "lecturers")
    public Iterable<LecturerSummaryDto> getLecturers() {
        logger.info("Fetching all lecturers");
        var lecturers = lecturerRepository.findAll()
                .stream()
                .map(lecturerMapper::toSummaryDto)
                .toList();
        logger.debug("Fetched {} lecturers", lecturers.size());
        return lecturers;
    }

    @Cacheable(value = "lecturerById", key = "#id")
    public LecturerDto getLecturerById(Long id) {
        logger.info("Fetching lecturer by id: {}", id);
        var lecturer = lecturerRepository.findById(id).orElse(null);
        if (lecturer == null) {
            logger.warn("Lecturer not found for id: {}", id);
            throw new LecturerNotFoundException();
        }
        logger.debug("Lecturer found: {} {}", lecturer.getName(), lecturer.getSurname());
        return lecturerMapper.toDto(lecturer);
    }

    public LecturerDto createLecturer(LecturerCreateDto lecturerCreateDto) {
        logger.info("Creating lecturer: {} {}", lecturerCreateDto.getName(), lecturerCreateDto.getSurname());
        Optional<Lecturer> existingLecturer = lecturerRepository.findByNameAndSurname(
                lecturerCreateDto.getName(),
                lecturerCreateDto.getSurname()
        );

        if (existingLecturer.isPresent()) {
            logger.warn("Lecturer already exists: {} {}", lecturerCreateDto.getName(), lecturerCreateDto.getSurname());
            throw new LecturerAlreadyExistsException();
        }

        var lecturer = lecturerMapper.toEntity(lecturerCreateDto);
        lecturerRepository.save(lecturer);
        logger.info("Lecturer created successfully: {} {}", lecturer.getName(), lecturer.getSurname());
        return lecturerMapper.toDto(lecturer);
    }
}
