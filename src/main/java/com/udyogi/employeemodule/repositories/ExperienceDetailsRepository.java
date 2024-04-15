package com.udyogi.employeemodule.repositories;

import com.udyogi.employeemodule.entities.ExperienceDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperienceDetailsRepository extends JpaRepository<ExperienceDetails, Long> {
}