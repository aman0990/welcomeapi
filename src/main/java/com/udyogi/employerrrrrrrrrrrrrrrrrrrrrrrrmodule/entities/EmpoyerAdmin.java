package com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class EmpoyerAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employer_id")
    private Long id;

    private String companyName;
    private String companyType;
    private String mobileNumber;
    private String email;
    private String address;
    private String companyUrl;
    private Integer numberOfEmployees;
    private Date establishedYear;
    private String incorporateId;
    private String password;
}
