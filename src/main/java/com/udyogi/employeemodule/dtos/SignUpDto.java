package com.udyogi.employeemodule.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SignUpDto {

    @NotNull(message = "First Name is required")
    @Size(min = 3, max = 50, message = "First Name must be between 3 and 50 characters")
    private String firstName;

    @NotNull(message = "Last Name is required")
    @Size(min = 3, max = 50, message = "Last Name must be between 3 and 50 characters")
    private String lastName;

    @NotNull(message = "Phone Number is required")
    @Size(min = 10, max = 13, message = "Phone Number must be between 3 and 50 characters")
    private String phoneNumber;

    @NotNull(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Password is required")
    private String password;

    @NotNull(message = "Gender is required")
    private String gender;
}