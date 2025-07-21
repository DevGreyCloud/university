package com.acme.university.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
public class StudentDto {
    private Long id;
    private String name;
    private String surname;
}
