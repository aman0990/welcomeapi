package com.udyogi.employeemodule.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class EducationDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private String instituteName;
    private String degree;
    private Boolean stillPursuing;
    private Date fromDate;
    private Date toDate;
    @Lob
    private byte[] resume;
    @OneToOne(fetch = FetchType.LAZY)
    private EmployeeEntity employee;
}
