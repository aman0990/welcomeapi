package com.udyogi.employeemodule.dtos;

import lombok.Value;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.udyogi.employeemodule.entities.EducationDetails}
 */
@Value
public class EducationDetailsDto implements Serializable {
    String instituteName;
    String degree;
    Boolean stillPursuing;
    Date fromDate;
    Date toDate;
    byte[] resume;
}