package com.gume.sonar.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gume.sonar.infrastructure.repository.entity.ReportEntity;

public interface ReportRepository extends JpaRepository<ReportEntity, UUID> {
    Optional<ReportEntity> findByIdAndUser_Id(UUID id, UUID userId);

    List<ReportEntity> findAllByUser_Id(UUID userId);
}
