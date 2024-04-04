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
@Table(name = "UserDetail")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "User_ID")
    private Long id;

    @Size(min = 3, max = 50, message = "Full Name must be between 3 and 50 characters")
    private String firstName;

    private String lastName;

    @Size(min = 3, max = 50, message = "Father's name must be between 3 and 50 characters")
    private String fathersName;

    private String gender;

    @Past
    @Column(name = "DOB", nullable = false)
    private LocalDate dateOfBirth;

    private String phoneNumber;

    @Size(max = 255, message = "Address can't exceed 255 characters")
    private String address;

    @Email
    private String email;

    @JsonIgnore
    private String createPassword;

    private String occupation;

    private String aadharCardNumber;

    @Lob
    @Column(length = 5000000)
    private byte[] profilePic;

    @ElementCollection
    private List<String> roles = new ArrayList<>();
    @JsonIgnore
    public boolean active;
    @JsonIgnore
    private String otp;

}