package com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities;

import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.enums.EmploymentType;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.enums.JobStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class JobPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Long id;

    private String jobTitle;
    private String experience;
    private int positions;
    @ElementCollection
    private List<String> skills = new ArrayList<>();

    private String education;
    private String location;
    @Enumerated(EnumType.STRING)
    private EmploymentType empolymentType;
    private String jobMode;
    private String salary;
    private String jobDescription;
    private String responsibilities;
    private String aboutCompany;
    @ElementCollection
    private List<String> questions=new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "employer_id")
    private EmployerAdmin employerAdmin;
    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date", insertable = false)
    private LocalDateTime updatedDate;
@Enumerated(EnumType.STRING)
    private JobStatus jobStatus;
}
