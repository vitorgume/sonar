package com.gume.sonar.application.gateway;

import com.gume.sonar.domain.Report;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReportGateway {
    
    Report save(Report report);
    
    Optional<Report> findById(UUID id);

    Optional<Report> findByIdAndUserId(UUID id, UUID userId);
    
    List<Report> findAll();

    List<Report> findAllByUserId(UUID userId);
    
    void deleteById(UUID id);
}
