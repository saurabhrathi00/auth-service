package com.auth_service.auth_service.repository;

import com.auth_service.auth_service.models.dao.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity,String> {
}
