package com.udyogi.employeemodule.repositories;

import com.udyogi.employeemodule.entities.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepo extends JpaRepository<EmployeeEntity, Long> {
    boolean existsByEmail(String email);

    EmployeeEntity findByEmail(String email);

    EmployeeEntity findByEmployeeId(Long id);
}
