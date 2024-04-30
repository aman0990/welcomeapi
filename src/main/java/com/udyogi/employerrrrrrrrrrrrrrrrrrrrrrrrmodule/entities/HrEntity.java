package com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class HrEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @JsonIgnore
    private Long hrId;
    private String hrName;
    private String email;
    @JsonIgnore
    private String hrPassword;
    @JsonIgnore
    private String hrMobile;
    private String hrDesignation;
    private String workLocation;
    @JsonIgnore
    private Boolean isHrActive;
    @JsonIgnore
    private Long otp;
    @Column(name = "role")
    @JsonIgnore
    private String role;
    @Lob
    private byte[] hrProfilePic;
    private String workExperience;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employer_id")
    @JsonBackReference
    @JsonIgnore
    private EmployerAdmin employerAdmin;
    @OneToMany(mappedBy = "hrEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JobPost> jobPosts = new LinkedHashSet<>();
}