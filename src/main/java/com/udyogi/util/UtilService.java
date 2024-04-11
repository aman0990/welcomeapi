package com.udyogi.util;

import com.udyogi.employeemodule.entities.EmployeeEntity;
import com.udyogi.employeemodule.repositories.EmployeeRepo;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.EmployerAdmin;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.repositories.EmployerAdminRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@AllArgsConstructor
public class UtilService {

    private final Random random = new Random();
    private final EmployeeRepo employeeRepo;
    private final EmployerAdminRepo employerAdminRepo;
    private final PasswordEncoder passwordEncoder;

    // Otp Generating 6 digits
    public int generateOtp() {
        return random.nextInt(900000) + 100000;
    }

    public boolean verifyPassword(String password, String password1) {
        return passwordEncoder.matches(password, password1);
    }

    public Boolean verifyEmail(String email, int otp) {
        if(email == null || otp == 0) {
            return false;
        }else if(employeeRepo.existsByEmail(email)){
            EmployeeEntity employeeEntity = employeeRepo.findByEmail(email);
            return employeeEntity.getOtp() == otp;
        } else if (employerAdminRepo.existsByEmail(email)) {
            EmployerAdmin employerAdmin = employerAdminRepo.findByEmail(email);
            return employerAdmin.getOtp() == otp;

        }else
            return false;
    }
}
