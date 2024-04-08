package com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.dtos;

import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.EmployerAdmin;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddJobPostDto {

    @NotBlank(message = "Job title cannot be blank")
    @Size(max = 255, message = "Job title length must be less than or equal to 255 characters")
    private String jobTitle;

    @NotBlank(message = "Experience cannot be blank")
    @Positive(message = "Positions must be a positive number")
    private String experience;

    @Positive(message = "Positions must be a positive number")
    private int positions;

    @NotBlank(message = "Skills cannot be blank")
    private String skills;

    @NotBlank(message = "Education cannot be blank")
    private String education;

    @NotBlank(message = "Location cannot be blank")
    private String location;

    @NotBlank(message = "Employment type cannot be blank")
    private String employmentType;

    @NotBlank(message = "Job mode cannot be blank")
    private String jobMode;

    @NotBlank(message = "Salary cannot be blank")
    private String salary;

    @NotBlank(message = "Job description cannot be blank")
    @Size(max = 1000, message = "Job description length must be less than or equal to 1000 characters")
    private String jobDescription;

    @NotBlank(message = "Responsibilities cannot be blank")
    @Size(max = 1000, message = "Responsibilities length must be less than or equal to 1000 characters")
    private String responsibilities;

    @NotBlank(message = "About company cannot be blank")
    @Size(max = 1000, message = "About company length must be less than or equal to 1000 characters")
    private String aboutCompany;

    @NotNull(message = "Questions list cannot be null")
    private List<@NotBlank(message = "Question cannot be blank") String> questions = new ArrayList<>();

}
