package com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.repositories;

import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.EmployerAdmin;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.HrEntity;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobPostRepo extends JpaRepository<JobPost, Long>{

    List<JobPost> findJobsBySkills(List<String> skills);

    List<JobPost> findTop5ByEmployerAdminOrderByCreatedDateDesc(EmployerAdmin employerAdmin);

    List<JobPost> findTop5ByHrEntityOrderByCreatedDateDesc(HrEntity hrEntity);

    List<JobPost> findByEmployerAdmin(EmployerAdmin employerAdmin);

    List<JobPost> findByHrEntity(HrEntity hrEntity);

    // Query method to find JobPosts by a list of HrEntity instances
    List<JobPost> findByHrEntityIn(List<HrEntity> hrEntities);
}
