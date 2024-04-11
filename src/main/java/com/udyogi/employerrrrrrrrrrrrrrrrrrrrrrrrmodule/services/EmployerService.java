package com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.services;

import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.dtos.AddJobPostDto;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.dtos.AdminSignUp;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.EmployerAdmin;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.JobPost;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.repositories.EmployerAdminRepo;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.repositories.JobPostRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class EmployerService {

    private final EmployerAdminRepo employerAdminRepo;
    private final JobPostRepo jobPostRepo;
    private final PasswordEncoder passwordEncoder;
    private static final String PREFIX = "UDY-";
    private static final String PADDING = "00000";
    private static int counter;


    public EmployerService(EmployerAdminRepo employerAdminRepo, JobPostRepo jobPostRepo, PasswordEncoder passwordEncoder) {
        this.employerAdminRepo = employerAdminRepo;
        this.jobPostRepo = jobPostRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public String addEmployer(AdminSignUp adminSignUp) {
        EmployerAdmin employerAdmin = new EmployerAdmin();
        employerAdmin.setCompanyName(adminSignUp.getCompanyName());
        employerAdmin.setCompanyType(adminSignUp.getCompanyType());
        employerAdmin.setMobileNumber(adminSignUp.getMobileNumber());
        employerAdmin.setEmail(adminSignUp.getEmail());
        employerAdmin.setAddress(adminSignUp.getAddress());
        employerAdmin.setCompanyUrl(adminSignUp.getCompanyUrl());
        employerAdmin.setNumberOfEmployees(adminSignUp.getNumberOfEmployees());
        employerAdmin.setEstablishedYear(adminSignUp.getEstablishedYear());
        employerAdmin.setIncorporateId(adminSignUp.getIncorporateId());
        employerAdmin.setAboutCompany(adminSignUp.getAboutCompany());
        employerAdmin.setPassword(passwordEncoder.encode(adminSignUp.getPassword()));
        employerAdmin.setCustomId(generateEmployerId(adminSignUp.getCompanyName()));
        employerAdminRepo.save(employerAdmin);
        return "Employer added successfully";
    }

    public String generateEmployerId(String companyName) {
        counter = employerAdminRepo.findAll().size();
        counter++;
        return PREFIX + companyName + PADDING.substring(String.valueOf(counter).length()) + counter;
    }

    public String loginEmployer(String email, String password) {
        EmployerAdmin employerAdmin = employerAdminRepo.findByEmail(email);
        if (Objects.isNull(employerAdmin)) {
            return "Employer not found";
        }
        if (passwordEncoder.matches(password, employerAdmin.getPassword())) {
            return "Employer logged in successfully";
        }
        return "Invalid credentials";
    }

    public String addJobPost(AddJobPostDto jobPost, Long id) {
        JobPost jobP = new JobPost();
        BeanUtils.copyProperties(jobPost, jobP);
        employerAdminRepo.findById(id).ifPresent(employerAdmin -> {
            employerAdmin.getJobPosts().add(jobP);
            employerAdminRepo.save(employerAdmin);
        });
        jobPostRepo.save(jobP);
        return null;
    }
}
