package com.udyogi.util;

import com.udyogi.employeemodule.repositories.EmployeeRepo;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.dtos.AdminSignUp;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.repositories.EmployerAdminRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomIdGenerator{

    private final EmployeeRepo employeeRepo;
    private static final String PREFIX = "UDY-";
    private static final String PADDING = "00000";
    private static int counter;

    public String generateEmployeeId() {
        counter = employeeRepo.findAll().size();
        counter++;
        return PREFIX + PADDING.substring(String.valueOf(counter).length()) + counter;
    }

    // UDY-COMPANYNAME-000001

}