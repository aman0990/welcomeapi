package com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.controller;

import com.udyogi.constants.UserConstants;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.dtos.*;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.EmployerAdmin;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.HrEntity;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.services.EmployerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/employer")
public class EmployerAdminController {

    private final EmployerService employerService;
    private static final Logger log = LoggerFactory.getLogger(EmployerAdminController.class);

    public EmployerAdminController(EmployerService employerService) {
        this.employerService = employerService;
    }


    @PostMapping("/signup")
    public ResponseEntity<String> addEmployer(@Valid @RequestBody AdminSignUp adminSignUp) {
        try {
            String msg = employerService.addEmployer(adminSignUp);
            log.info("Employer added successfully: {}", adminSignUp.getCompanyName());
            return new ResponseEntity<>(msg, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while adding employer", e);
            return new ResponseEntity<>("Error occurred while adding employer", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginEmployer(@RequestParam String email, @RequestParam String password) {
        try {
             employerService.loginEmployer(email, password);
            log.info("Employer logged in successfully: {}", email);
            return new ResponseEntity<>("Employer logged in successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while logging in employer", e);
            return new ResponseEntity<>("Error occurred while logging in employer", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add-jobpost/{customId}")
    public ResponseEntity<String> addJobPost(@RequestBody AddJobPostDto jobPost ,@PathVariable String customId) {
        try{
            String msg = employerService.addJobPost(jobPost,customId);
            log.info("Job post added successfully");
            return new ResponseEntity<>(msg, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while adding job post", e);
            return new ResponseEntity<>("Error occurred while adding job post", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // bhai update post test kr lena aata hun 10 mnt me !!
    @PutMapping("/update-jobpost/{id}")
    public ResponseEntity<String> updateJobPost(@RequestBody UpdateJobPostDto jobPost , @PathVariable Long id, @RequestParam String email) {
        try {
            employerService.updateJobPost(id, jobPost, email);
            log.info("Job post updated successfully");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while updating job post", e);
            return new ResponseEntity<>("Error occurred while updating job post", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-recent-job-posts/{email}")
    public ResponseEntity<CommonResponseDto> getRecentJobPost(@PathVariable String email) {
        try {
            CommonResponseDto commonResponseDto = employerService.getRecentJobPost(email).getBody();
            log.info("Recent job posts retrieved successfully");
            return new ResponseEntity<>(commonResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while retrieving recent job posts", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-all-users/{email}")
    public ResponseEntity<CommonResponseDto> getAllUsers(@PathVariable String email) {
        try {
            CommonResponseDto commonResponseDto = employerService.getAllUsers(email).getBody();
            log.info("All users retrieved successfully");
            return new ResponseEntity<>(commonResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while retrieving all users", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-all-job-applications/{email}")
    public ResponseEntity<CommonResponseDto> getAllJobApplications(@PathVariable String email) {
        try {
            CommonResponseDto commonResponseDto = employerService.getAllPosts(email).getBody();
            log.info("All job applications retrieved successfully");
            return new ResponseEntity<>(commonResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while retrieving all job applications", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // adding HR by sending OTP to HR email taking HR email as input and admin number
    @PostMapping("/add-hr/{email}")
    public ResponseEntity<String> addHr(@PathVariable String email, @RequestParam Long id) {
        try {
            String msg=employerService.addHr(email,id);
            log.info("HR added successfully");
            return new ResponseEntity<>(msg, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while adding HR", e);
            return new ResponseEntity<>("Error occurred while adding HR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/verify-Hr-Otp/{email}")
    public ResponseEntity<String> verifyHrOtp(@PathVariable String email, @RequestBody Map<String, String> requestBody) {
        try {
            String otpStr = requestBody.get("otp");
            String password = requestBody.get("password");
            Long otp = Long.valueOf(otpStr);
            String msg = employerService.verifyHrOtp(email, otp, password);
            log.info("HR verified successfully");
            return new ResponseEntity<>(msg, HttpStatus.OK);
        } catch (NumberFormatException e) {
            log.error("Invalid OTP format: " , e);
            return new ResponseEntity<>("Invalid OTP format", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error occurred while verifying HR", e);
            return new ResponseEntity<>("Error occurred while verifying HR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/Login-Hr-Profile")
    public ResponseEntity<CommonResponseDto> loginHrProfile(@RequestParam String email, @RequestParam String password) {
        try {
            var hrProfile = employerService.loginHrProfile(email, password);
            log.info("HR profile logged in successfully");
            if(hrProfile != null && HttpStatus.OK.equals(hrProfile.getStatusCode())) {
                return ResponseEntity.status(HttpStatus.OK).
                        body(new CommonResponseDto(hrProfile, UserConstants.LOGIN_SUCCESSFUL));
            } else {
                assert hrProfile != null;
                if (HttpStatus.NOT_FOUND.equals(hrProfile.getStatusCode())) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).
                            body(new CommonResponseDto(null, UserConstants.USER_NOT_FOUND_MESSAGE + email));
                } else if (HttpStatus.FORBIDDEN.equals(hrProfile.getStatusCode())) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).
                            body(new CommonResponseDto(null, UserConstants.HR_NOT_ACTIVE));
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).
                            body(new CommonResponseDto(null, UserConstants.INVALID_CREDENTIALS));
                }
            }
        }
        catch (Exception e) {
            log.error("Error occurred while logging in HR profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body(new CommonResponseDto(null, UserConstants.FAILED_TO_LOGIN_HR_PROFILE));
        }
    }

    @PutMapping("/update-Hr-Profile")
    public ResponseEntity<String> updateHrProfile(@RequestParam String email, @RequestBody HrCreateDto hrCreateDto) {
        try{
            var updated = employerService.updateHrProfile(email,hrCreateDto);
            log.info("HR profile updated successfully");
            if(updated != null){
                return ResponseEntity.status(HttpStatus.CREATED).
                        body(UserConstants.HR_ACCOUNT_CREATED_SUCCESSFULLY);
            }else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).
                        body(UserConstants.ERROR_WHILE_CREATING_HR_ACCOUNT);
            }
        } catch (Exception e) {
            log.error("Error occurred while updating HR profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body(UserConstants.FAILED_TO_CREATE_HR_ACCOUNT);
        }
    }

    @PutMapping("/update-profile-pic")
    public ResponseEntity<String> updateHrProfilePic(@RequestParam String email,
                                                     @RequestParam MultipartFile profilePic) {
        try {
            ResponseEntity<String> response = employerService.updateHrProfilePic(email, profilePic);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            log.error("Failed to update profile picture", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update profile picture");
        }
    }

    @GetMapping("/get-profile-photo")
    public ResponseEntity<byte[]> getProfilePhoto(@RequestParam String email) {
        try {
            byte[] profilePhoto = employerService.getProfilePhoto(email);
            if (profilePhoto != null && profilePhoto.length > 0) {
                return ResponseEntity.ok().body(profilePhoto);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Failed to get profile photo", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/all-job-posts/{employerCustomId}")
    public List<?> allJobPostsDTOS(@PathVariable Long employerCustomId){
        return employerService.getAllJobPosted(employerCustomId);
    }
}
