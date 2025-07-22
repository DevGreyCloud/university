package com.acme.university.dtos;

import lombok.Data;
import java.util.Set;

@Data
public class LecturerDto {
    private Long id;
    private String name;
    private String surname;
    private Set<StudentSummaryDto> students;
}
