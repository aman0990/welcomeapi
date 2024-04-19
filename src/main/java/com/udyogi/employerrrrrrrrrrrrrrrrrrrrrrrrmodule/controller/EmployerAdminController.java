package com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.controller;

import com.udyogi.constants.UserConstants;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.dtos.AddJobPostDto;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.dtos.AdminSignUp;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.dtos.HrCreateDto;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.services.EmployerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            e.printStackTrace();
            log.error("Error occurred while adding employer", e);
            return new ResponseEntity<>("Error occurred while adding employer", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    this is under development currently not woking
    @PostMapping("/login")
    public ResponseEntity<String> loginEmployer(@RequestParam String email, @RequestParam String password) {
        try {
             employerService.loginEmployer(email, password);
            log.info("Employer logged in successfully: {}", email);
            return new ResponseEntity<>("Employer logged in successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error occurred while logging in employer", e);
            return new ResponseEntity<>("Error occurred while logging in employer", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add-jobpost/{id}")
    public ResponseEntity<String> addJobPost(@RequestBody AddJobPostDto jobPost ,@PathVariable Long id) {
        try {
            String msg=employerService.addJobPost(jobPost,id);
            log.info("Job post added successfully");
            return new ResponseEntity<>(msg, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while adding job post", e);
            return new ResponseEntity<>("Error occurred while adding job post", HttpStatus.INTERNAL_SERVER_ERROR);
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
            // Extract the 'otp' value from the request body
            String otpStr = requestBody.get("otp");

            // Parse the 'otp' value to Long (if needed)
            // In this case, converting to Long is optional based on your service method's requirement
            Long otp = Long.valueOf(otpStr); // This might throw NumberFormatException if 'otpStr' is not a valid Long

            // Call the service method to verify OTP
            String msg = employerService.verifyHrOtp(email, otp);
            log.info("HR verified successfully");

            // Return success response
            return new ResponseEntity<>(msg, HttpStatus.OK);
        } catch (NumberFormatException e) {
            // Handle NumberFormatException (e.g., invalid 'otp' format)
            log.error("Invalid OTP format: " , e);
            return new ResponseEntity<>("Invalid OTP format", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Handle other exceptions
            log.error("Error occurred while verifying HR", e);
            return new ResponseEntity<>("Error occurred while verifying HR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update-Hr-Profile")
    public ResponseEntity<String> updateHrProfile(@RequestParam String email, @RequestBody HrCreateDto hrCreateDto) {
        try{
            var updated = employerService.updateHrProfile(email,hrCreateDto);
            log.info("HR profile updated successfully");
            if(updated != null){
                return ResponseEntity.status(HttpStatus.CREATED).body(UserConstants.HR_ACCOUNT_CREATED_SUCCESSFULLY);
            }else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(UserConstants.ERROR_WHILE_CREATING_HR_ACCOUNT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error occurred while updating HR profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(UserConstants.FAILED_TO_CREATE_HR_ACCOUNT);
        }
    }
}
