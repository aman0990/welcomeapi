package com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HrCreateDto {

    @Column(name = "hr_name", nullable = false)
    private String hrName;

    /*@Column(name = "hr_password", nullable = false)
    @JsonIgnore
    private String hrPassword;*/

    @Column(name = "hr_mobile", nullable = false)
    private String hrMobile;

    @Column(name = "hr_designation", nullable = false)
    private String hrDesignation;

    @Column(name = "work_location", nullable = false)
    private String workLocation;

    /*@Column(name = "hr_profile_pic", nullable = false)
    @Lob
    private byte[] hrProfilePic;*/

    @Column(name = "work_experience", nullable = false)
    private String workExperience;
}
