package com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.repositories;

import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobPostRepo extends JpaRepository<JobPost, Long>{
    List<JobPost> findJobsBySkill(String skill);

    //JobPost findByJobId(Long jobId);
}
