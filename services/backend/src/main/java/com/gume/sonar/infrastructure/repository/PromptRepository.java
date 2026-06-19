package com.gume.sonar.infrastructure.repository;

import com.gume.sonar.infrastructure.repository.entity.PromptEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PromptRepository extends JpaRepository<PromptEntity, UUID> {
}
