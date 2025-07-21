package com.acme.university.mappers;

import com.acme.university.dtos.LecturerDto;
import com.acme.university.dtos.StudentDto;
import com.acme.university.dtos.StudentSimplerDto;
import com.acme.university.entities.Lecturer;
import com.acme.university.entities.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LecturerMapper {
    @Mapping(source = "name", target = "name")
    @Mapping(source = "surname", target = "surname")
    @Mapping(source = "students", target = "students")
    LecturerDto toDto(Lecturer lecturer);

    // Add a method for simplified student mapping (without lecturers)
    @Mapping(source = "name", target = "name")
    @Mapping(source = "surname", target = "surname")
    StudentSimplerDto toStudentSimplerDto (Student student);

}
