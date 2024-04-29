package com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.dtos;

import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.JobPost;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link JobPost}
 */
@Value
public class UpdateJobPostDto implements Serializable {
    String jobTitle;
    String experience;
    String jobType;
    List<String> workMode;
    Integer positions;
    List<String> skills;
    String location;
    String jobMode;
    String salary;
    String jobDescription;
    String responsibilities;
    List<String> questions;
}