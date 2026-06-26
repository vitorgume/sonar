package com.gume.sonar.infrastructure.repository;

import com.gume.sonar.infrastructure.repository.entity.TranscricaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TranscricaoRepository extends JpaRepository<TranscricaoEntity, UUID> {
}
