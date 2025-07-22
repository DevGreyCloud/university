package com.acme.university.dtos;

import lombok.Data;
import java.util.Set;

@Data
public class StudentDto {
    private Long id;
    private String name;
    private String surname;
    private Set<LecturerCreateDto> lecturers;
}
