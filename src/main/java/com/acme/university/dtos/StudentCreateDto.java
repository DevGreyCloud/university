package com.acme.university.dtos;

import lombok.Data;

@Data
public class StudentCreateDto {
    private String name;
    private String surname;
    private Long lecturerId;
}
