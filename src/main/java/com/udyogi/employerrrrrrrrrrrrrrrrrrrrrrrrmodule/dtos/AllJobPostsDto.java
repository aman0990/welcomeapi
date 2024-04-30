package com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AllJobPostsDto {
    private long jobId;
    private String jobTitle;
    private String experience;
    private String salary;
    private Integer vacancies;
    private Integer applications;
    private Date createdDate;
    private String co_Ordinator_Name;
    private Boolean active;
}
