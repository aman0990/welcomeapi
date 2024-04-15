package com.udyogi.employeemodule.dtos;

import com.udyogi.employeemodule.entities.EmployeeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class loginResponseDto {

    private EmployeeEntity employeeEntity;
    private String message;
    private String status;

}
