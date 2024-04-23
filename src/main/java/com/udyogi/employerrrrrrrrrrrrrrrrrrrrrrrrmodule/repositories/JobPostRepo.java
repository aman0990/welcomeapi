package com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.repositories;

import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostRepo extends JpaRepository<JobPost, Long>{

    //JobPost findByJobId(Long jobId);
}
