package com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.services;

import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.dtos.AddJobPostDto;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.dtos.AdminSignUp;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.EmployerAdmin;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.JobPost;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.repositories.EmployerAdminRepo;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.repositories.JobPostRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class EmployerService {
    public final EmployerAdminRepo employerAdminRepo;
    public final JobPostRepo jobPostRepo;

    public EmployerService(EmployerAdminRepo employerAdminRepo, JobPostRepo jobPostRepo) {
        this.employerAdminRepo = employerAdminRepo;
        this.jobPostRepo = jobPostRepo;
    }

    public String addEmployer(AdminSignUp adminSignUp) {
        EmployerAdmin employerAdmin = new EmployerAdmin();
        BeanUtils.copyProperties(adminSignUp, employerAdmin);
        employerAdminRepo.save(employerAdmin);
        return "Employer added successfully";
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
