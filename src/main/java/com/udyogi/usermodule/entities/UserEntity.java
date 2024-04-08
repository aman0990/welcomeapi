package com.udyogi.usermodule.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "User_Details")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "User_ID")
    private Long id;

    @Size(min = 3, max = 50, message = "Full Name must be between 3 and 50 characters")
    private String firstName;

    private String lastName;


    private String phoneNumber;

    @Email
    private String email;

    @JsonIgnore
    private String password;


    private String gender;

    @Past
    @Column(name = "DOB", nullable = false)
    private LocalDate dateOfBirth;


    @Size(max = 255, message = "Address can't exceed 255 characters")
    private String address;


// resume required fields
private String resumeName;
    private String resumeType;
    private String resumePath;
    @Lob
    private byte[] resume;

// another required fields
    private String workStatus;
    private String accStatus;
    private boolean employed;
}