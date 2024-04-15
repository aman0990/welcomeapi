package com.udyogi.employeemodule.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Month;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    private String year;
    private Month month;
    private String noticePeriod;
    private Boolean freelancer;
    // experience details
    private String jobTitle;
    private String companyName;
    private String jobLocation;
    private String ctcInLpa;
    private Boolean stillWorking;
    private Date fromDate;
    private Date toDate;
    @OneToOne(cascade = CascadeType.ALL)
    private EmployeeEntity employee;
}
