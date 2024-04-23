package com.udyogi.employeemodule.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class JobPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id")
    private EmployeeEntity employee;
    @ElementCollection
    private List<String> jobRole;
    @ElementCollection
    private List<String> skills;
    @ElementCollection
    private List<String> jobType;
    @ElementCollection
    private List<String> jobLocation;
    private String experience;
    @ElementCollection
    private List<String> workMode;

}