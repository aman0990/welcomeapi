package com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.repositories;

import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.EmployerAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployerAdminRepo extends JpaRepository<EmployerAdmin, Long> {

    EmployerAdmin findByEmail(String email);

    boolean existsByEmail(String email);
}
