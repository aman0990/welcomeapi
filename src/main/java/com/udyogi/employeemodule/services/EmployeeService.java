package com.udyogi.employeemodule.services;

import com.udyogi.employeemodule.dtos.SignUpDto;
import com.udyogi.employeemodule.entities.EmployeeEntity;
import com.udyogi.employeemodule.mapper.EmployeeMapper;
import com.udyogi.employeemodule.repositories.EmployeeRepo;
import com.udyogi.util.EmailService;
import com.udyogi.util.UtilService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmployeeService {

    private final EmployeeRepo employeeRepo;
    private final EmailService emailService;
    private final UtilService utilService;
    private final PasswordEncoder passwordEncoder;

    public String signup(SignUpDto signUpDto) {
        if(signUpDto == null) {
            return "Invalid request";
        }
        try {
            EmployeeEntity employeeEntity = EmployeeMapper.mapSignUpDtoToEmployeeEntity(signUpDto);
            int otp = utilService.generateOtp();
            employeeEntity.setOtp(otp);
            employeeEntity.setVerified(false);
//            employeeEntity.
            if(employeeRepo.existsByEmail(signUpDto.getEmail())){
                return "Email already exists";
            }else {
                employeeEntity.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
                employeeRepo.save(employeeEntity);
                emailService.sendVerificationEmail(signUpDto.getEmail(),otp);
            }
        } catch (Exception e) {
            return "Failed to signup";
        }
        return "Employee signed up successfully";
    }

    public void verifyEmail(String email, Integer otp) {
        EmployeeEntity employeeEntity = employeeRepo.findByEmail(email);
        if(employeeEntity != null && employeeEntity.getOtp() == otp) {
            employeeEntity.setVerified(true);
            employeeRepo.save(employeeEntity);
        }
    }

    public void login(String email, String password) {
        EmployeeEntity employeeEntity = employeeRepo.findByEmail(email);
        if(employeeEntity != null && utilService.verifyPassword(password, employeeEntity.getPassword())) {
            System.out.println("Login successful");
        }
    }


}
