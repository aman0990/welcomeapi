/*
package com.udyogi.employeemodule;

import com.udyogi.employeemodule.entities.EmployeeEntity;
import com.udyogi.employeemodule.repositories.EmployeeRepo;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@AllArgsConstructor
public class DataLoader {

    private final EmployeeRepo employeeRepository;
    private final PasswordEncoder passwordEncoder;


    @PostConstruct
    public void loadEmployees() {
        List<EmployeeEntity> employees = generateEmployees(500);
        employeeRepository.saveAll(employees);
    }

    private List<EmployeeEntity> generateEmployees(int count) {
        List<EmployeeEntity> employees = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            EmployeeEntity employee = new EmployeeEntity();
            employee.setFirstName("FirstName" + i);
            employee.setLastName("LastName" + i);
            employee.setPhoneNumber(generateRandomPhoneNumber());
            employee.setEmail("employee" + i + "@example.com");
            employee.setPassword(passwordEncoder.encode("password"+i));
            employee.setGender(random.nextBoolean() ? "Male" : "Female");
            employee.setOtp(random.nextInt(10000));
            employee.setVerified(random.nextBoolean());
            employee.setCustomId(generateCustomId(i));
            employees.add(employee);
        }
        return employees;
    }

    private String generateRandomPhoneNumber() {
        Random random = new Random();
        StringBuilder phoneNumber = new StringBuilder("+");
        for (int i = 0; i < 10; i++) {
            phoneNumber.append(random.nextInt(10));
        }
        return phoneNumber.toString();
    }

    private String generateCustomId(int index) {
        return String.format("UDY-%06d", index + 1);
    }
}
*/
