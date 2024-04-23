package com.udyogi.employeemodule.entities;

import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.JobPost;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Setter
@Getter
@Entity

public class JobApplicationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;
    @ManyToOne
    @JoinColumn(name = "job_id")
    private JobPost jobPost;
    @CreatedDate
    private Date applicationDate;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employeeEntity;
    @Enumerated(EnumType.STRING)
    private ApplicationStatus applyStatus;
}
