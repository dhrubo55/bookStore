package com.example.mlab_assesment.repository;

import com.example.mlab_assesment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findDistinctByUsername(String username);
}
