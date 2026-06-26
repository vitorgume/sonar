package com.gume.sonar.infrastructure.repository;

import com.gume.sonar.infrastructure.repository.entity.PromptEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PromptRepository extends JpaRepository<PromptEntity, UUID> {
    Optional<PromptEntity> findByIdAndClient_User_Id(UUID id, UUID userId);

    Optional<PromptEntity> findByClient_IdAndClient_User_Id(UUID clientId, UUID userId);

    List<PromptEntity> findAllByClient_User_Id(UUID userId);
}
