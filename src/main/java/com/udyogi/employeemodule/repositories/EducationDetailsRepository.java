package com.udyogi.employeemodule.repositories;

import com.udyogi.employeemodule.entities.EducationDetails;
import com.udyogi.employeemodule.entities.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationDetailsRepository extends JpaRepository<EducationDetails, Long> {
    EducationDetails findByEmployee(EmployeeEntity employee);
}