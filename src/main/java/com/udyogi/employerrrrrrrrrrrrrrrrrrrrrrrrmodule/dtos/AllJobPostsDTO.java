package com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class AllJobPostsDTO {
    private Long id;
    private String jobTitle;
    private String experience;
  private Double salary;
  private Integer positions;

  private Integer applications;

  private Date dateOfPost;

  private String coOrdinator;
  private String status;
}
