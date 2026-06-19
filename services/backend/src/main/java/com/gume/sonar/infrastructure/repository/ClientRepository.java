package com.gume.sonar.infrastructure.repository;

import com.gume.sonar.infrastructure.repository.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientRepository extends JpaRepository<ClientEntity, UUID> {
}
