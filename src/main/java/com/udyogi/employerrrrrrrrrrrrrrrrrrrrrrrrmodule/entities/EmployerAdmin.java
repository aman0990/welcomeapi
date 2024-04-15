package com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class EmployerAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employer_id")
    private Long id;

    private String companyName;
    private String companyType;
    private String mobileNumber;
    private String email;
    private String address;
    private String companyUrl;
    private Integer numberOfEmployees;
    private Date establishedYear;
    private String incorporateId;
    private String aboutCompany;
    @JsonIgnore
    private String password;
    private Integer otp;
    private Boolean verified;
    @OneToMany(mappedBy = "employerAdmin", cascade = CascadeType.ALL)
    private List<JobPost> jobPosts = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date", insertable = false)
    private LocalDateTime updatedDate;

    // Custom ID generation for EmployerEntity
    @GenericGenerator(name = "custom-id-generator", strategy = "com.udyogi.util.CustomIdGenerator")
    @Column(name = "custom_id", nullable = false, unique = true, length = 50)
    private String customId;

    @OneToMany(mappedBy = "employerAdmin")
    private List<HrEntity> hrEntities;
}
