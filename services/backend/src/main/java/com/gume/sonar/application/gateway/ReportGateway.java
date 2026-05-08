package com.gume.sonar.application.gateway;

import com.gume.sonar.domain.Report;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReportGateway {
    
    Report save(Report report);
    
    Optional<Report> findById(UUID id);
    
    List<Report> findAll();
    
    void deleteById(UUID id);
}
