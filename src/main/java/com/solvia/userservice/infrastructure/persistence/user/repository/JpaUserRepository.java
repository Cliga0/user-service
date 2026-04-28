package com.solvia.userservice.infrastructure.persistence.user.repository;

import com.solvia.userservice.infrastructure.persistence.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<UserEntity, UUID> {
}
