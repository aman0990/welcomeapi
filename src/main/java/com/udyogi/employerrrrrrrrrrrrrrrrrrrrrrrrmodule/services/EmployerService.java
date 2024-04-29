package com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.services;

import com.udyogi.constants.UserConstants;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.dtos.*;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.EmployerAdmin;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.HrEntity;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.JobPost;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.repositories.EmployerAdminRepo;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.repositories.HrRepo;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.repositories.JobPostRepo;
import com.udyogi.util.EmailService;
import com.udyogi.util.PreAuthorizes;
import com.udyogi.util.UtilService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
                    employerAdmin.setRole("EMPLOYER_ADMIN");
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

    @Transactional
    public String addJobPost(AddJobPostDto jobPost, Long id) {
        JobPost jobP = new JobPost();
        BeanUtils.copyProperties(jobPost, jobP);
        try {
            Optional<EmployerAdmin> employerAdmin = employerAdminRepo.findById(id);
            Optional<HrEntity> hrEntity = hrRepo.findById(id);
            if(employerAdmin.isPresent()){
                jobP.setEmployerAdmin(employerAdmin.get());
                jobPostRepo.save(jobP);
                return "Job post added successfully for Employer Admin.";
            } else if(hrEntity.isPresent()) {
                jobP.setHrEntity(hrEntity.get());
                jobPostRepo.save(jobP);
                return "Job post added successfully for HR.";
            } else {
                return "User with ID " + id + " is not authorized as an Employer Admin or HR.";
            }
        } catch (EntityNotFoundException e) {
            // Log the entity not found exception
            log.error("Error adding job post: " + e.getMessage());
            return "User with ID " + id + " not found.";
        } catch (IllegalStateException e) {
            // Log the illegal state exception
            log.error("Error adding job post: " + e.getMessage());
            return e.getMessage();
        } catch (Exception e) {
            // Log any other unexpected exceptions
            log.error("Error adding job post", e);
            return "An unexpected error occurred while adding the job post.";
        }
    }

    // JOB Update
    @PreAuthorizes(roles = {"EMPLOYER_ADMIN","HR"},permissions = "UPDATE_JOB_POST",logical = PreAuthorizes.Logical.ALL)
    public ResponseEntity<String> updateJobPost(Long id, UpdateJobPostDto jobPost, String email){
        try {
            Optional<JobPost> jobPostOptional = jobPostRepo.findById(id);
            EmployerAdmin employerAdmin = employerAdminRepo.findByEmail(email);
            HrEntity hrEntity = hrRepo.findByEmail(email);
            if (jobPostOptional.isPresent()) {
                JobPost jobPostEntity = jobPostOptional.get();
                BeanUtils.copyProperties(jobPost, jobPostEntity);
                if(employerAdmin != null){
                    jobPostEntity.setEmployerAdmin(employerAdmin);
                    jobPostRepo.save(jobPostEntity);
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(UserConstants.JOB_POST_UPDATED_SUCCESSFULLY_BY_EMPLOYER_ADMIN);
                } else if(hrEntity != null) {
                    jobPostEntity.setHrEntity(hrEntity);
                    jobPostRepo.save(jobPostEntity);
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(UserConstants.JOB_POST_UPDATED_SUCCESSFULLY_BY_HR);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("User with ID " + id + " is not authorized as an Employer Admin or HR.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Job post with ID " + id + " not found.");
            }
        } catch (Exception e) {
            log.error("Error updating job post", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred while updating the job post.");
        }
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
        hrEntity.setEmail(email);
        hrEntity.setRole("HR");
        hrRepo.save(hrEntity);
        return "HR added successfully";
    }

    public ResponseEntity<String> updateHrProfile(String email, HrCreateDto hrCreateDto) {
        HrEntity hrEntity = hrRepo.findByEmail(email);
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
            hrRepo.save(hrEntity);
            emailService.sendConfirmationEmail(hrEntity.getEmail());
            return ResponseEntity.status(HttpStatus.OK).body(UserConstants.HR_PROFILE_UPDATED_SUCCESSFULLY);
        }
    }

    public String verifyHrOtp(String email, Long otp, String password) {
        if(Boolean.TRUE.equals(utilService.verifyEmail(email, otp))) {
            HrEntity hrEntity = hrRepo.findByEmail(email);
            hrEntity.setIsHrActive(true);
            hrEntity.setHrPassword(passwordEncoder.encode(password));
            hrRepo.save(hrEntity);
            return "HR verified successfully";
        }
        return "Error occurred while verifying HR";
    }

    public ResponseEntity<CommonResponseDto> loginHrProfile(String email, String password) {
        HrEntity hrEntity = hrRepo.findByEmail(email);
        if (hrEntity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body(new CommonResponseDto(null, UserConstants.USER_NOT_FOUND_MESSAGE + email));
        }
        if (Boolean.FALSE.equals(hrEntity.getIsHrActive())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).
                    body(new CommonResponseDto(null, UserConstants.HR_NOT_ACTIVE));
        }
        if (passwordEncoder.matches(password, hrEntity.getHrPassword())) {
            HrCreateDto hrCreateDto = new HrCreateDto();
            BeanUtils.copyProperties(hrEntity, hrCreateDto);
            return ResponseEntity.status(HttpStatus.OK).
                    body(new CommonResponseDto(hrCreateDto, UserConstants.LOGIN_SUCCESSFUL_MESSAGE));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).
                body(new CommonResponseDto(null, UserConstants.INVALID_CREDENTIALS));
    }

    @Transactional
    public ResponseEntity<String> updateHrProfilePic(String email, MultipartFile profilePic) {
        try {
            HrEntity hrEntity = hrRepo.findByEmail(email);
            if (hrEntity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("HR not found with email: " + email);
            }

            if (profilePic == null || profilePic.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("Profile picture cannot be null or empty");
            }

            hrEntity.setHrProfilePic(profilePic.getBytes());
            hrRepo.save(hrEntity);

            return ResponseEntity.ok("Profile picture updated successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update profile picture");
        }
    }

    public byte[] getProfilePhoto(String email) {
        HrEntity hrEntity = hrRepo.findByEmail(email);
        if (hrEntity != null && hrEntity.getHrProfilePic() != null) {
            return hrEntity.getHrProfilePic();
        } else {
            // Return default profile photo or handle the case when no profile photo is available
            return getDefaultProfilePhoto(); // Implement this method to return a default image
        }
    }

    private byte[] getDefaultProfilePhoto() {
        // Implement logic to load and return a default profile photo
        return new byte[0]; // Placeholder implementation
    }
}
