package com.acme.university.services;

import com.acme.university.dtos.*;
import com.acme.university.entities.Student;
import com.acme.university.exceptions.StudentAlreadyExistsException;
import com.acme.university.exceptions.StudentNotFoundException;
import com.acme.university.mappers.LecturerMapper;
import com.acme.university.mappers.StudentMapper;
import com.acme.university.repositories.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@AllArgsConstructor
@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final LecturerService lecturerService;
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final LecturerMapper lecturerMapper;

    @Cacheable(value = "students")
    public Iterable<StudentSummaryDto> getStudents() {
        logger.info("Fetching all students");
        var students = studentRepository.findAll()
                .stream()
                .map(studentMapper::toSummaryDto)
                .toList();
        logger.debug("Fetched {} students", students.size());
        return students;
    }

    @Cacheable(value = "studentById", key = "#id")
    public StudentDto getStudentById(Long id) {
        logger.info("Fetching student by id: {}", id);
        var student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            logger.warn("Student not found for id: {}", id);
            throw new StudentNotFoundException();
        }
        logger.debug("Student found: {} {}", student.getName(), student.getSurname());
        return studentMapper.toDto(student);
    }

    @Transactional
    public StudentDto createStudent(StudentCreateDto studentCreateDto) {
        logger.info("Creating student: {} {}", studentCreateDto.getName(), studentCreateDto.getSurname());
        Optional<Student> existingStudent = studentRepository.findByNameAndSurname(
                studentCreateDto.getName(),
                studentCreateDto.getSurname()
        );

        if (existingStudent.isPresent()) {
            logger.warn("Student already exists: {} {}", studentCreateDto.getName(), studentCreateDto.getSurname());
            throw new StudentAlreadyExistsException();
        }

        var lecturer = lecturerService.getLecturerById(studentCreateDto.getLecturerId());
        var student = studentMapper.toEntity(studentCreateDto);

        student.addLecturer(lecturerMapper.toEntity(lecturer));
        studentRepository.save(student);
        logger.info("Student created successfully: {} {}", student.getName(), student.getSurname());
        return studentMapper.toDto(student);
    }
}