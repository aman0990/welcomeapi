package com.udyogi.employeemodule.entities;

import org.hibernate.annotations.GenericGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Long employeeId;
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
    private Boolean fresher;
    private String role;
    @Lob
    private byte[] profilePic;
    @GenericGenerator(name = "custom-id-generator", strategy = "com.udyogi.util.CustomIdGenerator")
    @Column(name = "custom_id", nullable = false, unique = true, length = 50)
    private String customId;

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id", referencedColumnName = "employer_id")
    private EmployerAdmin employer;*/
}