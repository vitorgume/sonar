package com.gume.sonar.infrastructure.repository;

import com.gume.sonar.infrastructure.repository.entity.TranscricaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TranscricaoRepository extends JpaRepository<TranscricaoEntity, UUID> {
}
