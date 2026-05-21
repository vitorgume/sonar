package com.gume.sonar.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gume.sonar.infrastructure.repository.entity.ReportEntity;

public interface ReportRepository extends JpaRepository<ReportEntity, UUID> {}
