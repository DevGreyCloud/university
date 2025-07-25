package com.acme.university.mappers;

import com.acme.university.dtos.*;
import com.acme.university.entities.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "surname", target = "surname")
    @Mapping(source = "lecturers", target = "lecturers")
    StudentDto toDto(Student student);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "surname", target = "surname")
    StudentSummaryDto toSummaryDto(Student student);

    Student toEntity(StudentCreateDto studentCreateDto);
}
