package com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.repositories;
import com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.entities.EmployerAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface EmployerRepositories extends JpaRepository<EmployerAdmin, Long> {
    Object findByemail(String email);
}
