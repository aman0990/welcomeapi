package com.udyogi.employeemodule.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.EmployerAdmin;
import com.udyogi.util.CustomIdGenerator;
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
    // Custom ID generation for EmployeeEntity
    /*UDY-000001*/
    @GenericGenerator(name = "custom-id-generator", strategy = "com.udyogi.util.CustomIdGenerator")
    @Column(name = "custom_id", nullable = false, unique = true, length = 50)
    private String customId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id", referencedColumnName = "employer_id")
    private EmployerAdmin employer;
}