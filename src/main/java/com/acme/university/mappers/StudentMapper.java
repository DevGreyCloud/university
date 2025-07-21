package com.acme.university.mappers;

import com.acme.university.dtos.LecturerDto;
import com.acme.university.dtos.LecturerSimplerDto;
import com.acme.university.dtos.StudentDto;
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
    LecturerSimplerDto toLecturerSimplerDto(Lecturer lecturer);
}
