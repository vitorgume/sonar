package com.gume.sonar.infrastructure.repository;

import com.gume.sonar.infrastructure.repository.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<ClientEntity, UUID> {

    Optional<ClientEntity> findByIdAndUser_Id(UUID id, UUID userId);

    List<ClientEntity> findAllByUser_Id(UUID userId);

    void deleteByIdAndUser_Id(UUID id, UUID userId);
}
