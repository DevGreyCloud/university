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
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class StudentService {
    private final LecturerService lecturerService;
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final LecturerMapper lecturerMapper;

    public Iterable<StudentDto> getStudents() {
        return studentRepository.findAll()
                .stream()
                .map(studentMapper::toDto)
                .toList();
    }

    public StudentDto getStudentById(Long id) {
        var student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            throw new StudentNotFoundException();
        }
        return studentMapper.toDto(student);
    }

    @Transactional
    public StudentDto createStudent(StudentCreateDto studentCreateDto) {
        Optional<Student> existingStudent = studentRepository.findByNameAndSurname(
                studentCreateDto.getName(),
                studentCreateDto.getSurname()
        );

        if (existingStudent.isPresent()) {
            throw new StudentAlreadyExistsException();
        }

        var lecturer = lecturerService.getLecturerById(studentCreateDto.getLecturerId());
        var student = studentMapper.toEntity(studentCreateDto);

        student.addLecturer(lecturer);
        studentRepository.save(student);

        return studentMapper.toDto(student);
    }

    public StudentResponseDto createStudentResponseDto(StudentDto studentDto) {
        return studentMapper.toStudentResponseDto(studentDto);
    }
}