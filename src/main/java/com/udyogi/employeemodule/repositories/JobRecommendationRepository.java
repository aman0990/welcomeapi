package com.udyogi.employeemodule.repositories;

import com.udyogi.employeemodule.entities.JobRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRecommendationRepository extends JpaRepository<JobRecommendation, Long> {
}