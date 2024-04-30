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
public class AllUsersData {

    private Long id;
    private String co_Ordinator_Name;
    private String co_Ordinator_Email;
    private String numberOfPosts;
    private Integer numberOfApplicationReceived;
    private Date jobPostDate;
    private Boolean active;
}