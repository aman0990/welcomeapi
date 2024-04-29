package com.udyogi.employeemodule.dtos;

import com.udyogi.employeemodule.entities.ApplicationStatus;
import com.udyogi.employeemodule.entities.EmployeeEntity;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.JobPost;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.udyogi.employeemodule.entities.JobApplicationEntity}
 */
@Value
public class JobApplicationEntityDto implements Serializable {
    @NotNull
    JobPost jobPost;
    @NotNull
    EmployeeEntity employeeEntity;
    @NotNull
    ApplicationStatus applyStatus;
}