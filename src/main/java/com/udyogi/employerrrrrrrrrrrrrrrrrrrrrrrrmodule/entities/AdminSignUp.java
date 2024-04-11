package com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminSignUp {
    @NotBlank(message = "Company name is required")
    @Size(min = 2, max = 100, message = "Company name must be between 2 and 100 characters")
    private String companyName;

    @NotBlank(message = "Company type is required")
    @Size(min = 2, max = 50, message = "Company type must be between 2 and 50 characters")
    private String companyType;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits")
    private String mobileNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String address;

    @Size(max = 255, message = "Company URL cannot exceed 255 characters")
    private String companyUrl;

    @NotNull(message = "Number of employees is required")
    @Positive(message = "Number of employees must be positive")
    private Integer numberOfEmployees;

    @PastOrPresent(message = "Established year must be in the past or present")
    private Date establishedYear;

    @NotBlank(message = "Incorporate ID is required")
    @Size(max = 50, message = "Incorporate ID cannot exceed 50 characters")
    private String incorporateId;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
}
