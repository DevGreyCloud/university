package com.acme.university.mappers;

import com.acme.university.dtos.*;
import com.acme.university.entities.Lecturer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LecturerMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "surname", target = "surname")
    @Mapping(source = "students", target = "students")
    LecturerDto toDto(Lecturer lecturer);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "surname", target = "surname")
    LecturerResponseDto toLecturerResponseDto(Lecturer lecturer);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "surname", target = "surname")
    LecturerResponseDto toLecturerResponseDto(LecturerDto lecturer);

    Lecturer toEntity(LecturerCreateDto lecturerCreateDto);

    Lecturer toEntity(LecturerResponseDto lecturerResponseDto);
}
