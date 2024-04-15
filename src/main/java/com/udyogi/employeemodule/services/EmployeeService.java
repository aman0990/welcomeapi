package com.udyogi.employeemodule.services;

import com.udyogi.constants.UserConstants;
import com.udyogi.employeemodule.dtos.SignUpDto;
import com.udyogi.employeemodule.entities.EmployeeEntity;
import com.udyogi.employeemodule.mapper.EmployeeMapper;
import com.udyogi.employeemodule.repositories.EmployeeRepo;
import com.udyogi.util.CustomIdGenerator;
import com.udyogi.util.EmailService;
import com.udyogi.util.UtilService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepo employeeRepo;
    private final EmailService emailService;
    private final UtilService utilService;
    private final CustomIdGenerator customIdGenerator;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<String> signup(SignUpDto signUpDto) {
        try {
            if (signUpDto == null) {
                throw new IllegalArgumentException("SignUpDto is null");
            }
            EmployeeEntity existingEmployee = employeeRepo.findByEmail(signUpDto.getEmail());
            if (existingEmployee != null) {
                return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(UserConstants.USER_WITH_USERNAME_OR_EMAIL_ALREADY_EXISTS);
            }
            EmployeeEntity employeeEntity = EmployeeMapper.mapSignUpDtoToEmployeeEntity(signUpDto);
            int otp = utilService.generateOtp();
            employeeEntity.setOtp(otp);
            employeeEntity.setVerified(false);
            employeeEntity.setRole("EMPLOYEE");
            if(Objects.equals(signUpDto.getGender(), "male")) employeeEntity.setGender("MALE");
            if(Objects.equals(signUpDto.getGender(), "female")) employeeEntity.setGender("FEMALE");
            if(Objects.equals(signUpDto.getGender(), "other")) employeeEntity.setGender("OTHER");
            employeeEntity.setCustomId(customIdGenerator.generateEmployeeId());
            employeeEntity.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
            employeeRepo.save(employeeEntity);
            emailService.sendVerificationEmail(signUpDto.getEmail(), otp);
            return ResponseEntity.status(HttpStatus.CREATED).body(UserConstants.USER_ACCOUNT_CREATED_SUCCESSFULLY);
        } catch (IllegalArgumentException | DataAccessException e) {
            logger.error("Error occurred during employee signup", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(UserConstants.FAILED_TO_CREATE_USER_ACCOUNT);
        }
    }

    public ResponseEntity<Boolean> verifyEmail(String email, Integer otp) {
        try {
            EmployeeEntity employeeEntity = employeeRepo.findByEmail(email);
            if (employeeEntity != null && employeeEntity.getOtp().equals(otp)) {
                employeeEntity.setVerified(true);
                employeeRepo.save(employeeEntity);
                return ResponseEntity.status(HttpStatus.OK).
                        body(UserConstants.ACCOUNT_VERIFIED_SUCCESSFULLY);
            } else if (employeeEntity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).
                        body(Boolean.valueOf(UserConstants.USER_NOT_FOUND));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).
                        body(UserConstants.INVALID_OTP);
            }
        }
        catch (DataAccessException e) {
            logger.error("Error occurred during employee email verification", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body(Boolean.valueOf(UserConstants.FAILED_TO_VERIFY_ACCOUNT));
        }
    }

    public ResponseEntity<String> login(String email, String password) {
        try {
            EmployeeEntity employeeEntity = employeeRepo.findByEmail(email);
            if (employeeEntity != null && passwordEncoder.matches(password, employeeEntity.getPassword())) {
                return ResponseEntity.status(HttpStatus.OK).body(UserConstants.LOGIN_SUCCESSFUL);
            } else if(employeeEntity == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(UserConstants.USER_NOT_FOUND);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(UserConstants.INVALID_CREDENTIALS);
            }
        } catch (DataAccessException e) {
            logger.error("Error occurred during employee login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(UserConstants.FAILED_TO_PROCEED_LOGIN);
        }
    }
}