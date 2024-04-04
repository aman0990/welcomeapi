package com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminSignUp {
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
