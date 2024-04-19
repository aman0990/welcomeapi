package com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.services;

import com.udyogi.constants.UserConstants;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.dtos.AddJobPostDto;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.dtos.AdminSignUp;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.dtos.HrCreateDto;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.EmployerAdmin;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.HrEntity;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.JobPost;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.repositories.EmployerAdminRepo;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.repositories.HrRepo;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.repositories.JobPostRepo;
import com.udyogi.util.EmailService;
import com.udyogi.util.UtilService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class EmployerService {

    private final EmployerAdminRepo employerAdminRepo;
    private final HrRepo hrRepo;
    private final UtilService utilService;
    private final EmailService emailService;
    private final JobPostRepo jobPostRepo;
    private final PasswordEncoder passwordEncoder;
    private static final String PREFIX = "UDY-";
    private static final String PADDING = "00000";


    public String addEmployer(AdminSignUp adminSignUp) {
        return Optional.ofNullable(adminSignUp)
                .map(admin -> {
                    EmployerAdmin employerAdmin = new EmployerAdmin();
                    BeanUtils.copyProperties(adminSignUp, employerAdmin);
                    employerAdmin.setOtp(utilService.generateOtp());
                    employerAdmin.setVerified(false);
                    employerAdmin.setPassword(passwordEncoder.encode(adminSignUp.getPassword()));
                    employerAdmin.setCustomId(generateEmployerId(adminSignUp.getCompanyName()));
                    employerAdminRepo.save(employerAdmin);
                    emailService.sendVerificationEmail(adminSignUp.getEmail(), Math.toIntExact(employerAdmin.getOtp()));
                    return "Employer added successfully";
                })
                .orElse("Employer not found");
        /*EmployerAdmin employerAdmin = new EmployerAdmin();
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
        employerAdmin.setOtp(utilService.generateOtp());
        employerAdmin.setVerified(false);
        employerAdmin.setPassword(passwordEncoder.encode(adminSignUp.getPassword()));
        employerAdmin.setCustomId(generateEmployerId(adminSignUp.getCompanyName()));
        emailService.sendVerificationEmail(adminSignUp.getEmail(), employerAdmin.getOtp());
        employerAdminRepo.save(employerAdmin);
        return "Employer added successfully";*/
    }

    public String generateEmployerId(String companyName) {
        int counter = employerAdminRepo.findAll().size();
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

    public String addHr(String email, Long id) {
        HrEntity hrEntity = new HrEntity();
        var hrEmail = hrRepo.findByEmail(email);
        if(hrEmail != null) {
            return "HR already exists";
        }
        var otp = utilService.generateOtp();
        hrEntity.setOtp(otp);
        emailService.sendOtptoHr(email, Math.toIntExact(otp));
        hrEntity.setEmployerAdmin(employerAdminRepo.findById(id).get());
        hrRepo.save(hrEntity);
        return "HR added successfully";
    }

    public ResponseEntity<String> updateHrProfile(String email, HrCreateDto hrCreateDto) {
        HrEntity hrEntity = new HrEntity();
        if(Objects.isNull(hrCreateDto)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(UserConstants.NOT_ACCEPTABLE_406);
        }
        if(Boolean.FALSE.equals(hrEntity.getIsHrActive())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(UserConstants.HR_NOT_ACTIVE);
        }else {
            hrEntity.setHrDesignation(hrCreateDto.getHrDesignation());
            hrEntity.setHrMobile(hrCreateDto.getHrMobile());
            hrEntity.setHrName(hrCreateDto.getHrName());
            hrEntity.setWorkExperience(hrCreateDto.getWorkExperience());
            hrEntity.setWorkLocation(hrCreateDto.getWorkLocation());
            hrEntity.setHrProfilePic(hrCreateDto.getHrProfilePic());
            hrEntity.setHrPassword(passwordEncoder.encode(hrCreateDto.getHrPassword()));
            var hrEmail = employerAdminRepo.findByEmail(email);
            if (Objects.nonNull(hrEmail)) {
                employerAdminRepo.save(hrEmail);
                emailService.sendConfirmationEmail(hrEmail.getEmail());
                return ResponseEntity.status(HttpStatus.CREATED).body(UserConstants.HR_ACCOUNT_CREATED_SUCCESSFULLY);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(UserConstants.FAILED_TO_CREATE_USER_ACCOUNT);
            }
        }
    }

    public String verifyHrOtp(String email, Long otp) {
        if(Boolean.TRUE.equals(utilService.verifyEmail(email, otp))) {
            HrEntity hrEntity = new HrEntity();
            hrEntity.setIsHrActive(true);
            hrRepo.save(hrEntity);
            updateHrProfile(email, new HrCreateDto());
            return "HR verified successfully";
        }
        return "Error occurred while verifying HR";
    }
}
