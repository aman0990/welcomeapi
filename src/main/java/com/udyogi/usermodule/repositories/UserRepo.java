package com.udyogi.usermodule.repositories;

import com.udyogi.usermodule.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<UserEntity, Long> {

}
