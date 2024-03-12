package com.burakkaya.userservice.repository;

import com.burakkaya.userservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>{
}
