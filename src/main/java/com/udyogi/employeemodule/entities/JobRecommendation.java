package com.udyogi.employeemodule.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @ElementCollection
    private List<String> preferredJobs = new ArrayList<>();
    @ElementCollection
    private List<String> preferredSkills = new ArrayList<>();
    @ElementCollection
    private List<String> preferredLocations = new ArrayList<>();
    @ElementCollection
    private List<String> preferredTypes = new ArrayList<>();
    private String preferredExp;
    @ElementCollection
    private List<String> preferredWorkMode = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employee;
    @Column(name = "job_name")
    private String jobName;
}
