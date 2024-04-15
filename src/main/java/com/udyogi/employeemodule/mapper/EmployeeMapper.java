package com.udyogi.employeemodule.mapper;

import com.udyogi.employeemodule.dtos.SignUpDto;
import com.udyogi.employeemodule.entities.EmployeeEntity;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    /**
     * Maps SignUpDto to EmployeeEntity.
     *
     * @param signUpDto SignUpDto object
     * @return EmployeeEntity mapped from SignUpDto
     */
    public static EmployeeEntity mapSignUpDtoToEmployeeEntity(SignUpDto signUpDto) {
        if (signUpDto == null) {
            return null;
        }
        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setFirstName(signUpDto.getFirstName());
        employeeEntity.setLastName(signUpDto.getLastName());
        employeeEntity.setPhoneNumber(signUpDto.getPhoneNumber());
        employeeEntity.setEmail(signUpDto.getEmail());
        employeeEntity.setPassword(signUpDto.getPassword()); // Note: Password should be encrypted before storing
        employeeEntity.setGender(signUpDto.getGender());
        return employeeEntity;
    }
}
