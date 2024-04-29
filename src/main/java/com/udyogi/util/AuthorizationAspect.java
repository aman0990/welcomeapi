package com.udyogi.util;

import com.udyogi.employeemodule.entities.EmployeeEntity;
import com.udyogi.employeemodule.repositories.EmployeeRepo;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.EmployerAdmin;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.repositories.EmployerAdminRepo;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.HrEntity;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.repositories.HrRepo;
import lombok.AllArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Aspect
@Component
@AllArgsConstructor
public class AuthorizationAspect {

    private final EmployeeRepo employeeRepo;
    private final EmployerAdminRepo employerAdminRepo;
    private final HrRepo hrRepo;

    @Before("@annotation(com.udyogi.util.PreAuthorizes) && args(email, ..)")
    public void authorize(String email) {
        Object userEntity = getUserByEmail(email);
        if (userEntity != null) {
            if (userEntity instanceof EmployeeEntity employee && isValidEmployee(employee))
                    return;
             else if (userEntity instanceof EmployerAdmin employerAdmin && isValidEmployerAdmin(employerAdmin) 
                    || (userEntity instanceof HrEntity hr) && (isValidHr(hr)))
                return;
        }
        throw new RuntimeException("Unauthorized access");
    }

    private Object getUserByEmail(String email) {
        return Stream.of(employeeRepo.findByEmail(email),
                        employerAdminRepo.findByEmail(email),
                        hrRepo.findByEmail(email))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private boolean isValidEmployee(EmployeeEntity employee) {
        return Optional.ofNullable(employee)
                .map(EmployeeEntity::getVerified)
                .orElse(false);
    }

    private boolean isValidEmployerAdmin(EmployerAdmin employerAdmin) {
        return Optional.ofNullable(employerAdmin)
                .map(EmployerAdmin::getVerified)
                .orElse(false);
    }

    private boolean isValidHr(HrEntity hr) {
        return Optional.ofNullable(hr)
                .map(HrEntity::getIsHrActive)
                .orElse(false);
    }
}