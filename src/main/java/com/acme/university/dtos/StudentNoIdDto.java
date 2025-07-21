package com.acme.university.dtos;

import lombok.Data;

import java.util.Set;

@Data
public class StudentNoIdDto {
    private String name;
    private String surname;
    private Set<LecturerSimplerDto> lecturers;
}
