package com.udyogi.employeemodule.controller;

import com.udyogi.constants.UserConstants;
import com.udyogi.employeemodule.dtos.SignUpDto;
import com.udyogi.employeemodule.dtos.loginDto;
import com.udyogi.employeemodule.services.EmployeeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employee")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    // employee signups
    @PostMapping("/employeeSignUp")
    public ResponseEntity<String> employeeSignUp(@Valid @RequestBody SignUpDto signUpDto) {
        if(signUpDto == null) {
            return ResponseEntity.badRequest().body(UserConstants.BAD_REQUEST_400);
        }else {
             employeeService.signup(signUpDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(UserConstants.USER_ACCOUNT_CREATED_SUCCESSFULLY);
        }
    }

    // employee Verification
    @PostMapping("/verifyEmail/{email}/{otp}")
    public ResponseEntity<String> employeeVerifyEmail(@PathVariable String email, @PathVariable Integer otp) {
        if(email == null || otp == 0) {
            return ResponseEntity.badRequest().body(UserConstants.BAD_REQUEST_400);
        }else {
            employeeService.verifyEmail(email, otp);
            return ResponseEntity.status(HttpStatus.OK).body(UserConstants.ACCOUNT_VERIFIED_SUCCESSFULLY);
        }
    }

    // employee login
    @PostMapping("/login")
    public ResponseEntity<String> employeeLogin(@RequestBody loginDto loginDto) {
        if(loginDto.getEmail()== null || loginDto.getPassword() == null) {
            return ResponseEntity.badRequest().body(UserConstants.BAD_REQUEST_400);
        }else {
            employeeService.login(loginDto.getEmail(), loginDto.getPassword());
            return ResponseEntity.status(HttpStatus.OK).body(UserConstants.LOGIN_SUCCESSFUL);
        }
    }
}
