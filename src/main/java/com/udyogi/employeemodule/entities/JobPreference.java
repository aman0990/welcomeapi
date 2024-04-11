package com.udyogi.employeemodule.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JobPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id")
    private EmployeeEntity employee;
    private List<String> jobRole;
    private List<String> skills;
    private List<String> jobType;
    private List<String> jobLocation;
    private String experience;
    private List<String> workMode;
}