package com.acme.university.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
public class LecturerDto {
    private Long id;
    private String name;
    private String surname;
}
