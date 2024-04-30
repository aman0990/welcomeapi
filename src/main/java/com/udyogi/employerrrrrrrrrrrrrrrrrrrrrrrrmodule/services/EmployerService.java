package com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.services;

import com.udyogi.constants.UserConstants;
import com.udyogi.employeemodule.entities.JobApplicationEntity;
import com.udyogi.employeemodule.repositories.JobApplicationEntityRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@AllArgsConstructor
public class EmployerService {

    private final EmployerAdminRepo employerAdminRepo;
    private final HrRepo hrRepo;
    private final UtilService utilService;
    private final EmailService emailService;
    private final JobPostRepo jobPostRepo;
    private final JobApplicationEntityRepository jobApplicationEntityRepository;
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
                    employerAdmin.setEmployerCustomId(generateEmployerIds(adminSignUp.getCompanyName()));
                    employerAdmin.setRole("EMPLOYER_ADMIN");
                    employerAdminRepo.save(employerAdmin);
                    emailService.sendVerificationEmail(adminSignUp.getEmail(), Math.toIntExact(employerAdmin.getOtp()));
                    return "Employer added successfully";
                })
                .orElse("Employer not found");
    }

    public String generateEmployerId(String companyName) {
        int counter = employerAdminRepo.findAll().size();
        counter++;
        return PREFIX + companyName + PADDING.substring(String.valueOf(counter).length()) + counter;
    }

    // input chaman Company
    // expected output UDY-AD-chaman00004
    private String generateEmployerIds(String companyName) {
        var PREFIXED = "UDY-AD-";
        var PADDED = "00000";
        var counter = employerAdminRepo.findAll().size();
        counter++;
        var paddedCounter = String.format("%05d", counter);
        return PREFIXED + companyName.split(" ")[0] + PADDED.substring(paddedCounter.length()) + paddedCounter;
    }

    private String generateHrIds(String companyName) {
        var PREFIXED = "UDY-HR-";
        var PADDED = "00000";
        var counter = employerAdminRepo.findAll().size();
        counter++;
        var paddedCounter = String.format("%05d", counter);
        return PREFIXED + companyName.split(" ")[0] + PADDED.substring(paddedCounter.length()) + paddedCounter;
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
    public String addJobPost(AddJobPostDto jobPost, String customId) {
        JobPost jobP = new JobPost();
        BeanUtils.copyProperties(jobPost, jobP);
        try {
            Optional<EmployerAdmin> employerAdmin = employerAdminRepo.findByEmployerCustomId(customId);
            Optional<HrEntity> hrEntity = hrRepo.findByHrCustomId(customId);
            if(employerAdmin.isPresent()){
                if(Boolean.TRUE.equals(employerAdmin.get().getVerified())) {
                    jobP.setEmployerAdmin(employerAdmin.get());
                    jobPostRepo.save(jobP);
                    return "Job post added successfully for Employer Admin.";
                } else {
                    return "Employer Admin with ID " + customId + " is not verified.";
                }
            } else if(hrEntity.isPresent()) {
                if(Boolean.TRUE.equals(hrEntity.get().getIsHrActive())) {
                    jobP.setHrEntity(hrEntity.get());
                    jobPostRepo.save(jobP);
                    return "Job post added successfully for HR.";
                } else {
                    return "HR with ID " + customId + " is not active.";
                }
            } else {
                return "User with ID " + customId + " is not authorized as an Employer Admin or HR.";
            }
        } catch (EntityNotFoundException e) {
            log.error("Error adding job post: " + e.getMessage());
            return "User with ID " + customId + " not found.";
        } catch (IllegalStateException e) {
            log.error("Error adding job posts: " + e.getMessage());
            return e.getMessage();
        } catch (Exception e) {
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

    // Recent Job Post
    public ResponseEntity<CommonResponseDto> getRecentJobPost(String email) {
        try {
            EmployerAdmin employerAdmin = employerAdminRepo.findByEmail(email);
            HrEntity hrEntity = hrRepo.findByEmail(email);
            if (employerAdmin != null) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new CommonResponseDto(jobPostRepo.findTop5ByEmployerAdminOrderByCreatedDateDesc(employerAdmin),
                                UserConstants.RECENT_JOB_POSTS_BY_EMPLOYER_ADMIN));
            } else if (hrEntity != null) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new CommonResponseDto(jobPostRepo.findTop5ByHrEntityOrderByCreatedDateDesc(hrEntity),
                                UserConstants.RECENT_JOB_POSTS_BY_HR));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new CommonResponseDto(null, "User with email " + email + " not found."));
            }
        } catch (Exception e) {
            log.error("Error getting recent job posts", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CommonResponseDto(null, UserConstants.ERROR_GETTING_RECENT_JOB_POSTS));
        }
    }

    // All Users
    public ResponseEntity<CommonResponseDto> getAllUsers(String email) {
        try{
            EmployerAdmin employerAdmin = employerAdminRepo.findByEmail(email);
            List<HrEntity> hrEntity = hrRepo.findByEmployerAdmin(employerAdmin);
            if(!hrEntity.isEmpty()){
                AllUsersData allUsersData = new AllUsersData();
                hrEntity.forEach(hr -> {
                    allUsersData.setId(hr.getHrId());
                    allUsersData.setCo_Ordinator_Name(hr.getHrName());
                    allUsersData.setCo_Ordinator_Email(hr.getEmail());
                    allUsersData.setNumberOfPosts(String.valueOf(hr.getJobPosts().size()));
                    JobApplicationEntity jobApplicationEntity = new JobApplicationEntity();
                    var jobPosts = jobApplicationEntity.getJobPost();
                    allUsersData.setJobPostDate(jobPosts.getCreatedDate());
                    allUsersData.setActive(jobPostRepo.findById(jobPosts.getId()).get().getActive());
                });
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new CommonResponseDto(allUsersData, UserConstants.ALL_USERS));
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new CommonResponseDto(null, UserConstants.USER_NOT_FOUND + ": " +email));
            }
        } catch (Exception e) {
            log.error("Error getting all users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CommonResponseDto(null, UserConstants.ERROR_GETTING_ALL_USERS));
        }
    }

    public String addHr(String email, Long id) {
        HrEntity hrEntity = new HrEntity();
        var hrEmail = hrRepo.findByEmail(email);
        if(hrEmail != null) {
            return "HR already exists";
        }
        var otp = utilService.generateOtp();
        var employer = employerAdminRepo.findById(id).get();
        hrEntity.setOtp(otp);
        emailService.sendOtptoHr(email, Math.toIntExact(otp));
        hrEntity.setEmployerAdmin(employer);
        hrEntity.setEmail(email);
        hrEntity.setHrCustomId(generateHrIds(employer.getCompanyName()));
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
            log.error("Error updating profile picture", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update profile picture");
        }
    }

    public byte[] getProfilePhoto(String email) {
        HrEntity hrEntity = hrRepo.findByEmail(email);
        if (hrEntity != null && hrEntity.getHrProfilePic() != null) {
            return hrEntity.getHrProfilePic();
        } else {
            return getDefaultProfilePhoto();
        }
    }

    private byte[] getDefaultProfilePhoto() {
        return new byte[0];
    }

    public List<?> getAllJobPosted(Long employerCustomId) {
        // Find the EmployerAdmin by employerCustomId
        Optional<EmployerAdmin> employerAdminOptional = employerAdminRepo.findById(employerCustomId);

        // Check if EmployerAdmin exists
        if (employerAdminOptional.isPresent()) {
            // Get the list of HrEntities associated with the EmployerAdmin
            EmployerAdmin employerAdmin = employerAdminOptional.get();
            List<JobPost> jobPosts = employerAdmin.getJobPosts();
            return jobPosts;
        } else {
            // If EmployerAdmin with the given ID does not exist, return an empty list
            return Collections.emptyList();
        }

//        .stream()
//                .flatMap(hr -> {
//                    EmployerAdmin employerAdmin = hr.getEmployerAdmin();
//                    if (employerAdmin != null) {
//                        return employerAdmin.getJobPosts().stream()
//                                .map(jobPost -> {
//                                    AllJobPostsDTO dto = new AllJobPostsDTO();
//                                    dto.setId(jobPost.getId());
//                                    dto.setJobTitle(jobPost.getJobTitle());
//                                    dto.setExperience(jobPost.getExperience());
//                                    dto.setSalary(Double.valueOf(jobPost.getSalary()));
//                                    dto.setPositions(jobPost.getPositions());
//                                    dto.setDateOfPost(jobPost.getCreatedDate());
//                                    dto.setCoOrdinator(hr.getHrName());
//                                    dto.setStatus(String.valueOf(jobPost.getJobStatus()));
//                                    return dto;
//                                });
//                    } else {
//                        return Stream.empty(); // Return an empty stream if employerAdmin is null
//                    }
//                })
//                .collect(Collectors.toList());
    }


}
