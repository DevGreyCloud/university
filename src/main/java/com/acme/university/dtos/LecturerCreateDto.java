package com.acme.university.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LecturerCreateDto {
    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Name must contain only letters and numbers")
    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @NotBlank(message = "Surname is required")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Surname must contain only letters and numbers")
    @Size(max = 255, message = "Surname must be less than 255 characters")
    private String surname;
}
