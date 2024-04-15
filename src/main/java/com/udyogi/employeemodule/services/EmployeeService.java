package com.udyogi.employeemodule.services;

import com.udyogi.constants.UserConstants;
import com.udyogi.employeemodule.dtos.*;
import com.udyogi.employeemodule.entities.EducationDetails;
import com.udyogi.employeemodule.entities.EmployeeEntity;
import com.udyogi.employeemodule.entities.ExperienceDetails;
import com.udyogi.employeemodule.mapper.EmployeeMapper;
import com.udyogi.employeemodule.repositories.EducationDetailsRepository;
import com.udyogi.employeemodule.repositories.EmployeeRepo;
import com.udyogi.employeemodule.repositories.ExperienceDetailsRepository;
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
    private final ExperienceDetailsRepository experienceDetailsRepository;
    private final EducationDetailsRepository educationDetailsRepository;

    public ResponseEntity<String> signup(SignUpDto signUpDto) {
        try {
            if (signUpDto == null) {
                throw new IllegalArgumentException("SignUpDto is null");
            }
            EmployeeEntity existingEmployee = employeeRepo.findByEmail(signUpDto.getEmail());
            if (existingEmployee != null) {
                return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).
                        body(UserConstants.USER_WITH_USERNAME_OR_EMAIL_ALREADY_EXISTS);
            }
            EmployeeEntity employeeEntity = EmployeeMapper.mapSignUpDtoToEmployeeEntity(signUpDto);
            int otp = utilService.generateOtp();
            employeeEntity.setOtp(otp);
            employeeEntity.setVerified(false);
            employeeEntity.setRole("EMPLOYEE");
            if (Objects.equals(signUpDto.getGender(), "male")) employeeEntity.setGender("MALE");
            if (Objects.equals(signUpDto.getGender(), "female")) employeeEntity.setGender("FEMALE");
            if (Objects.equals(signUpDto.getGender(), "other")) employeeEntity.setGender("OTHER");
            employeeEntity.setCustomId(customIdGenerator.generateEmployeeId());
            employeeEntity.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
            employeeRepo.save(employeeEntity);
            emailService.sendVerificationEmail(signUpDto.getEmail(), otp);
            return ResponseEntity.status(HttpStatus.CREATED).
                    body(UserConstants.USER_ACCOUNT_CREATED_SUCCESSFULLY);
        } catch (IllegalArgumentException | DataAccessException e) {
            logger.error("Error occurred during employee signup", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body(UserConstants.FAILED_TO_CREATE_USER_ACCOUNT);
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
        } catch (DataAccessException e) {
            logger.error("Error occurred during employee email verification", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body(Boolean.valueOf(UserConstants.FAILED_TO_VERIFY_ACCOUNT));
        }
    }

    public ResponseEntity<loginResponseDto> login(loginDto loginDto) {
        try {
            EmployeeEntity employeeEntity = employeeRepo.findByEmail(loginDto.getEmail());
            if (employeeEntity != null && passwordEncoder.matches(
                    loginDto.getPassword(), employeeEntity.getPassword())) {
                return ResponseEntity.status(HttpStatus.OK).
                        body(new loginResponseDto(employeeEntity,
                                UserConstants.LOGIN_SUCCESSFUL,
                                UserConstants.LOGIN_SUCCESSFUL_MESSAGE));
            } else if (employeeEntity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).
                        body(new loginResponseDto(null,
                                UserConstants.USER_NOT_FOUND,
                                UserConstants.USER_NOT_FOUND_MESSAGE));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).
                        body(new loginResponseDto(null,
                                UserConstants.INVALID_CREDENTIALS,
                                UserConstants.INVALID_CREDENTIALS));
            }
        } catch (DataAccessException e) {
            logger.error("Error occurred during employee login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body(new loginResponseDto(null,
                            UserConstants.FAILED_TO_PROCEED_LOGIN,
                            UserConstants.FAILED_TO_PROCEED_LOGIN));
        }
    }

    public ResponseEntity<String> addExperienceDetails(Long id, ExperienceDetailsDto experienceDetailsDto) {
        try {
            if (experienceDetailsDto == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                        body(UserConstants.USER_DETAILS_CAN_NOT_BE_NULL);
            }
            EmployeeEntity employeeEntity = employeeRepo.findByEmployeeId(id);
            if (employeeEntity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).
                        body(UserConstants.USER_NOT_FOUND + " " + id);
            }else {
                ExperienceDetails experienceDetails = EmployeeMapper.
                        mapExperienceDetailsDtoToExperienceDetails(experienceDetailsDto);
                experienceDetails.setEmployee(employeeEntity);

                experienceDetailsRepository.save(experienceDetails);
                return ResponseEntity.status(HttpStatus.OK).
                        body(UserConstants.EXPERIENCE_DETAILS_ADDED_SUCCESSFULLY);
            }
        } catch (DataAccessException e) {
            logger.error("Error occurred during adding experience details", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body(UserConstants.FAILED_TO_ADD_EXPERIENCE_DETAILS);
        }
    }


    public ResponseEntity<String> addEducationDetails(Long id, EducationDetailsDto educationDetailsDto) {
        try {
            if (educationDetailsDto == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                        body(UserConstants.USER_DETAILS_CAN_NOT_BE_NULL);
            }
            EmployeeEntity employeeEntity = employeeRepo.findByEmployeeId(id);
            if (employeeEntity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).
                        body(UserConstants.USER_NOT_FOUND + " " + id);
            }else {
                EducationDetails educationDetails = EmployeeMapper.
                        mapEducationDetailsDtoToEducationDetails(educationDetailsDto);
                educationDetails.setEmployee(employeeEntity);
                educationDetailsRepository.save(educationDetails);
                return ResponseEntity.status(HttpStatus.OK).
                        body(UserConstants.EDUCATION_DETAILS_ADDED_SUCCESSFULLY);
            }
        } catch (DataAccessException e) {
            logger.error("Error occurred during adding education details", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body(UserConstants.FAILED_TO_ADD_EDUCATION_DETAILS);
        }
    }
}