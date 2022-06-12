package com.example.bookstore.repository;

import com.example.bookstore.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    List<RoleEntity> findByIdIn(List<Long> ids);
}
