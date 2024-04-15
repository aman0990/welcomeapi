package com.udyogi.employeemodule.controller;

import com.udyogi.constants.UserConstants;
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

@RestController
@RequestMapping("/api/v1/employee")
@AllArgsConstructor
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeService employeeService;

    // employee signups
    @PostMapping("/employeeSignUp")
    public ResponseEntity<String> employeeSignUp(@Valid @RequestBody SignUpDto signUpDto) {
        try {
            if (signUpDto == null) {
                return ResponseEntity.badRequest().body(UserConstants.BAD_REQUEST_400);
            }
            var signedUp = employeeService.signup(signUpDto);
            if(signedUp.getStatusCode().equals(HttpStatus.CREATED)){
                return ResponseEntity.status(HttpStatus.CREATED).body(UserConstants.USER_ACCOUNT_CREATED_SUCCESSFULLY);
            }else if(signedUp.getStatusCode().equals(HttpStatus.ALREADY_REPORTED)){
                return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(UserConstants.USER_WITH_USERNAME_OR_EMAIL_ALREADY_EXISTS);
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(UserConstants.FAILED_TO_CREATE_USER_ACCOUNT);
            }
        } catch (Exception e) {
            logger.error("Error occurred during employee signup", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(UserConstants.FAILED_TO_CREATE_USER_ACCOUNT);
        }
    }

    // Employee email verification
    @PostMapping("/verifyEmail/{email}/{otp}")
    public ResponseEntity<String> employeeVerifyEmail(@PathVariable String email, @PathVariable Integer otp) {
        try {
            if (email == null || otp == null) {
                return ResponseEntity.badRequest().body(UserConstants.BAD_REQUEST_400);
            }
            Boolean verified = employeeService.verifyEmail(email, otp);
            if (Boolean.TRUE.equals(verified)) {
                return ResponseEntity.ok(UserConstants.ACCOUNT_VERIFIED_SUCCESSFULLY);
            } else {
                return ResponseEntity.badRequest().body(UserConstants.FAILED_TO_VERIFY_ACCOUNT);
            }
        } catch (IllegalArgumentException e) {
            logger.error("Invalid input provided for email verification: email={}, otp={}", email, otp);
            return ResponseEntity.badRequest().body(UserConstants.INVALID_INPUT_PROVIDED_FOR_EMAIL_VERIFICATION);
        } catch (Exception e) {
            logger.error("Error occurred during employee email verification", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(UserConstants.ERROR_WHILE_VERIFYING_ACCOUNT);
        }
    }

    // employee login
    @PostMapping("/login")
    public ResponseEntity<String> employeeLogin(@Valid @RequestBody loginDto loginDto) {
        try {
            if (loginDto == null) {
                return ResponseEntity.badRequest().body(UserConstants.BAD_REQUEST_400);
            }
            var loggedIn = employeeService.login(loginDto.getEmail(), loginDto.getPassword());
            if(loggedIn.getStatusCode().equals(HttpStatus.OK)){
                return ResponseEntity.ok(UserConstants.LOGIN_SUCCESSFUL);
            } else if (loggedIn.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(UserConstants.USER_NOT_FOUND + " "+loginDto.getEmail());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(UserConstants.INVALID_CREDENTIALS);
            }
        } catch (Exception e) {
            logger.error("Error occurred during employee login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(UserConstants.FAILED_TO_PROCEED_LOGIN);
        }
    }
}
