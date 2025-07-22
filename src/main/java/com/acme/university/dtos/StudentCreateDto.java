package com.acme.university.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class StudentCreateDto {
    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Name must contain only letters and numbers")
    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @NotBlank(message = "Surname is required")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Surname must contain only letters and numbers")
    @Size(max = 255, message = "Surname must be less than 255 characters")
    private String surname;

    @NotNull(message = "Lecturer ID is required")
    @PositiveOrZero(message = "Lecturer ID must be greater than or equal to zero")
    private Long lecturerId;
}
