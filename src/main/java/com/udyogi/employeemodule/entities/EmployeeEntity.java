package com.udyogi.employeemodule.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "custom-id-generator")
    @Column(name = "employee_id", nullable = false, unique = true, length = 20)
    private String employeeId;

    @GeneratedValue(generator = "custom-id-generator")
    @GenericGenerator(name = "custom-id-generator", strategy = "com.udyogi.employeemodule.entities.CustomIdGenerator")
    @Column(name = "custom_id", nullable = false, unique = true, length = 50)
    private String customId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    @JsonIgnore
    private String password;
    private String gender;
    @JsonIgnore
    private Integer otp;
    private Boolean verified;
}