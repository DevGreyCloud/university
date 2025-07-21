package com.acme.university.mappers;

import com.acme.university.dtos.LecturerDto;
import com.acme.university.entities.Lecturer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LecturerMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "surname", target = "surname")
    LecturerDto toDto(Lecturer lecturer);
}
