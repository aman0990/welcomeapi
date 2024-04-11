package com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.repositories;

import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.HrEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HrRepo extends JpaRepository<HrEntity, Long> {
}
