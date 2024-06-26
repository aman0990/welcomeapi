package com.udyogi.employeemodule.repositories;

import com.udyogi.employeemodule.entities.EmployeeEntity;
import com.udyogi.employeemodule.entities.JobApplicationEntity;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface JobApplicationEntityRepository extends JpaRepository<JobApplicationEntity, Long>{

    @Transactional
    JobApplicationEntity findByJobPostAndEmployeeEntity(JobPost jobPost, EmployeeEntity employeeEntity);
}