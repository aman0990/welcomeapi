package com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.repositories;

import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.EmployerAdmin;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.HrEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HrRepo extends JpaRepository<HrEntity, Long> {
    boolean existsByEmail(String email);

    HrEntity findByEmail(String email);

    List<HrEntity> findByEmployerAdmin(EmployerAdmin employerAdmin);

    Optional<HrEntity> findByHrCustomId(String customId);
}
