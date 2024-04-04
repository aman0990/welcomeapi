package com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class JobPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Long id;

    private String jobTitle;
    private String experience;
    private int positions;
    private String skills;

    private String education;
    private String location;
    private String empolymentType;
    private String jobMode;
    private String salary;
    private String jobDescription;
    private String responsibilities;
    private String aboutCompany;
    @ElementCollection
    private List<String> questions=new ArrayList<>();


}
