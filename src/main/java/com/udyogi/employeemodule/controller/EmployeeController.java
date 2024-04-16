package com.udyogi.employeemodule.controller;

import com.udyogi.constants.UserConstants;
import com.udyogi.employeemodule.dtos.EducationDetailsDto;
import com.udyogi.employeemodule.dtos.ExperienceDetailsDto;
import com.udyogi.employeemodule.dtos.SignUpDto;
import com.udyogi.employeemodule.dtos.loginDto;
import com.udyogi.employeemodule.services.EmployeeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/employee")
@AllArgsConstructor
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeService employeeService;

    /**
     * Endpoint to handle employee sign up.
     *
     * @param signUpDto SignUpDto object containing user signup details
     * @return ResponseEntity indicating the outcome of the sign-up process
     */
    @PostMapping("/employeeSignUp")
    public ResponseEntity<String> employeeSignUp(@Valid @RequestBody SignUpDto signUpDto) {
        try {
            logger.info("Received request to sign up employee with data: {}", signUpDto);
            if (signUpDto == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(UserConstants.USER_DETAILS_CAN_NOT_BE_NULL);
            }
            var signedUp = employeeService.signup(signUpDto);
            if (signedUp.getStatusCode().equals(HttpStatus.CREATED)) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(UserConstants.USER_ACCOUNT_CREATED_SUCCESSFULLY);
            } else if (signedUp.getStatusCode().equals(HttpStatus.ALREADY_REPORTED)) {
                return ResponseEntity.status(HttpStatus.ALREADY_REPORTED)
                        .body(UserConstants.USER_WITH_USERNAME_OR_EMAIL_ALREADY_EXISTS);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(UserConstants.FAILED_TO_CREATE_USER_ACCOUNT);
            }
        } catch (Exception e) {
            logger.error("Error occurred during employee sign up", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(UserConstants.FAILED_TO_CREATE_USER_ACCOUNT);
        }
    }
    @GetMapping("/get")
    public String home() {
        return "Welcome to Udyogi Employee Module";
    }

    /**
     * Endpoint to verify employee's email with OTP.
     *
     * @param email Employee's email address
     * @param otp   One-time password (OTP) for verification
     * @return ResponseEntity indicating the outcome of the email verification process
     */
    @PostMapping("/verifyEmail/{email}/{otp}")
    public ResponseEntity<String> employeeVerifyEmail(@PathVariable String email, @PathVariable Integer otp) {
        try {
            logger.info("Received request to verify email: {}, OTP: {}", email, otp);
            var verified = employeeService.verifyEmail(email, otp);
            if (verified.getStatusCode().equals(HttpStatus.OK)) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(String.valueOf(UserConstants.ACCOUNT_VERIFIED_SUCCESSFULLY));
            } else if (verified.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(UserConstants.USER_NOT_FOUND + " " + email);
            } else if (verified.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(String.valueOf(UserConstants.INVALID_OTP));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(UserConstants.ERROR_WHILE_VERIFYING_ACCOUNT);
            }
        } catch (Exception e) {
            logger.error("Error occurred during employee email verification", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(UserConstants.FAILED_TO_VERIFY_ACCOUNT);
        }
    }

    /**
     * Endpoint to handle employee login.
     *
     * @param loginDto loginDto object containing user login details
     * @return ResponseEntity indicating the outcome of the login process
     */
    @PostMapping("/login")
    public ResponseEntity<String> employeeLogin(@Valid @RequestBody loginDto loginDto) {
        try {
            logger.info("Received request to log in with email: {}", loginDto.getEmail());
            var loggedIn = employeeService.login(loginDto);
            if (loggedIn.getStatusCode().equals(HttpStatus.OK)) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(UserConstants.LOGIN_SUCCESSFUL);
            } else if (loggedIn.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(UserConstants.USER_NOT_FOUND_MESSAGE + " " + loginDto.getEmail());
            } else if (loggedIn.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(UserConstants.INVALID_CREDENTIALS);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(UserConstants.FAILED_TO_PROCEED_LOGIN);
            }
        } catch (Exception e) {
            logger.error("Error occurred during employee login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(UserConstants.FAILED_TO_PROCEED_LOGIN);
        }
    }

    /**
     * Endpoint to add experience details for an employee.
     *
     * @param id                    Employee ID
     * @param experienceDetailsDto  ExperienceDetailsDto containing experience details
     * @return ResponseEntity indicating the outcome of adding experience details
     */
    @PostMapping("/addExperienceDetails/{id}")
    public ResponseEntity<String> addExperienceDetails(@PathVariable Long id, @Valid @RequestBody ExperienceDetailsDto experienceDetailsDto) {
        try {
            logger.info("Received request to add experience details for user with ID: {}", id);
            if (experienceDetailsDto == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(UserConstants.USER_DETAILS_CAN_NOT_BE_NULL);
            }
            var added = employeeService.addExperienceDetails(id, experienceDetailsDto);
            if (added.getStatusCode().equals(HttpStatus.OK)) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(UserConstants.EXPERIENCE_DETAILS_ADDED_SUCCESSFULLY);
            } else if (added.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(UserConstants.USER_NOT_FOUND_MESSAGE + " " + id);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(UserConstants.FAILED_TO_ADD_EXPERIENCE_DETAILS);
            }
        } catch (Exception e) {
            logger.error("Error occurred during adding experience details", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(UserConstants.FAILED_TO_ADD_EXPERIENCE_DETAILS);
        }
    }

    /**
     * Endpoint to add education details for an employee.
     *
     * @param id                   Employee ID
     * @param educationDetailsDto  EducationDetailsDto containing education details
     * @return ResponseEntity indicating the outcome of adding education details
     */
    @PostMapping("/addEducationDetails/{id}")
    public ResponseEntity<String> addEducationDetails(@PathVariable Long id, @Valid @RequestBody EducationDetailsDto educationDetailsDto) {
        try {
            logger.info("Received request to add education details for user with ID: {}", id);
            if (educationDetailsDto == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(UserConstants.USER_DETAILS_CAN_NOT_BE_NULL);
            }
            var added = employeeService.addEducationDetails(id, educationDetailsDto);
            if (added.getStatusCode().equals(HttpStatus.OK)) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(UserConstants.EDUCATION_DETAILS_ADDED_SUCCESSFULLY);
            } else if (added.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(UserConstants.USER_NOT_FOUND_MESSAGE + " " + id);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(UserConstants.FAILED_TO_ADD_EDUCATION_DETAILS);
            }
        } catch (Exception e) {
            logger.error("Error occurred during adding education details", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(UserConstants.FAILED_TO_ADD_EDUCATION_DETAILS);
        }
    }

    /**
     * Endpoint to add profile picture for an employee.
     *
     * @param id   Employee ID
     * @param file MultipartFile containing the profile picture file
     * @return ResponseEntity indicating the outcome of adding the profile picture
     */
    @PostMapping("/addProfilePic/{id}")
    public ResponseEntity<String> addProfilePic(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            logger.info("Received request to add profile picture for user with ID: {}", id);

            if (file == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(UserConstants.FILE_CAN_NOT_BE_NULL);
            }

            var added = employeeService.addProfilePic(id, file);
            if (added.getStatusCode().equals(HttpStatus.OK)) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(UserConstants.PROFILE_PIC_ADDED_SUCCESSFULLY);
            } else if (added.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(UserConstants.USER_NOT_FOUND_MESSAGE + " " + id);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(UserConstants.FAILED_TO_ADD_PROFILE_PIC);
            }
        } catch (Exception e) {
            logger.error("Error occurred during adding profile picture", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(UserConstants.FAILED_TO_ADD_PROFILE_PIC);
        }
    }

    /**
     * Endpoint to add resume for an employee.
     *
     * @param id   Employee ID
     * @param file MultipartFile containing the resume file
     * @return ResponseEntity indicating the outcome of adding the resume
     */
    @PostMapping("/addResume/{id}")
    public ResponseEntity<String> addResume(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            logger.info("Received request to add resume for user with ID: {}", id);

            if (file == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(UserConstants.FILE_CAN_NOT_BE_NULL);
            }

            var added = employeeService.addResume(id, file);
            if (added.getStatusCode().equals(HttpStatus.OK)) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(UserConstants.RESUME_ADDED_SUCCESSFULLY);
            } else if (added.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(UserConstants.USER_NOT_FOUND_MESSAGE + " " + id);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(UserConstants.FAILED_TO_ADD_RESUME);
            }
        } catch (Exception e) {
            logger.error("Error occurred during adding resume", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(UserConstants.FAILED_TO_ADD_RESUME);
        }
    }
}