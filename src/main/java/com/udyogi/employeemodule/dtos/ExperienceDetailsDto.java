package com.udyogi.employeemodule.dtos;

import java.io.Serializable;
import java.time.Month;
import java.util.Date;

/**
 * DTO for {@link com.udyogi.employeemodule.entities.ExperienceDetails}
 */
public record ExperienceDetailsDto(String year, Month month, String noticePeriod, Boolean freelancer, String jobTitle,
                                   String companyName, String jobLocation, String ctcInLpa, Boolean stillWorking,
                                   Date fromDate, Date toDate) implements Serializable {
}