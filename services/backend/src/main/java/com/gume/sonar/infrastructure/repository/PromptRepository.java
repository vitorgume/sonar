package com.gume.sonar.infrastructure.repository;

import com.gume.sonar.infrastructure.repository.entity.PromptEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PromptRepository extends JpaRepository<PromptEntity, UUID> {

    Optional<PromptEntity> findByIdAndUser_Id(UUID id, UUID userId);

    List<PromptEntity> findAllByUser_Id(UUID userId);
}
