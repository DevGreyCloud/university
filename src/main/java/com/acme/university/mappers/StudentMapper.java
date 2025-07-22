package com.acme.university.mappers;

import com.acme.university.dtos.*;
import com.acme.university.entities.Lecturer;
import com.acme.university.entities.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    @Mapping(source = "name", target = "name")
    @Mapping(source = "surname", target = "surname")
    @Mapping(source = "lecturers", target = "lecturers")
    StudentDto toDto(Student student);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "surname", target = "surname")
    StudentCreateDto toStudentCreateDto(Student student);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "surname", target = "surname")
    StudentResponseDto toStudentResponseDto(Student student);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "surname", target = "surname")
    StudentResponseDto toStudentResponseDto(StudentDto studentDto);

    Student toEntity(StudentCreateDto studentCreateDto);
}
