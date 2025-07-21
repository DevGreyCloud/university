package com.acme.university.dtos;

import lombok.Data;

import java.util.Set;

@Data
public class LecturerNoIdDto {
    private String name;
    private String surname;
    private Set<StudentSimplerDto> students;
}
