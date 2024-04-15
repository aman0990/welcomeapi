package com.udyogi.util;

import com.udyogi.employeemodule.entities.EmployeeEntity;
import com.udyogi.employeemodule.repositories.EmployeeRepo;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.EmployerAdmin;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.HrEntity;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.repositories.EmployerAdminRepo;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.repositories.HrRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.UUID;

@Service
public class UtilService {

    private final Random random = new Random();
    private final EmployeeRepo employeeRepo;
    private final HrRepo hrRepo;
    private final EmployerAdminRepo employerAdminRepo;
    private final PasswordEncoder passwordEncoder;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public UtilService(EmployeeRepo employeeRepo, HrRepo hrRepo, EmployerAdminRepo employerAdminRepo, PasswordEncoder passwordEncoder) {
        this.employeeRepo = employeeRepo;
        this.hrRepo = hrRepo;
        this.employerAdminRepo = employerAdminRepo;
        this.passwordEncoder = passwordEncoder;
    }

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
        }else if (hrRepo.existsByEmail(email)) {
            HrEntity hrEntity = hrRepo.findByEmail(email);
            return hrEntity.getOtp() == otp;
        }else
            return false;
    }

    public String storeFile(MultipartFile file) throws FileStorageException {
        try {
            // Create the directory if it doesn't exist
            Path directory = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(directory);
            // Generate a unique file name
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            // Set the target location
            Path targetLocation = directory.resolve(fileName);
            // Copy the file to the target location
            Files.copy(file.getInputStream(), targetLocation);
            return fileName; // Return the stored file name or path
        } catch (IOException ex) {
            throw new FileStorageException("Failed to store file " + file.getOriginalFilename(), ex);
        }
    }
}
