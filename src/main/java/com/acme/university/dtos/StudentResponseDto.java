package com.acme.university.dtos;

import lombok.Data;

import java.util.Set;

@Data
public class StudentResponseDto {
    private String name;
    private String surname;
    private Set<LecturerCreateDto> lecturers;
}
