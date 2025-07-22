package com.acme.university.dtos;

import lombok.Data;

@Data
public class StudentSummaryDto {
    private Long id;
    private String name;
    private String surname;
}
