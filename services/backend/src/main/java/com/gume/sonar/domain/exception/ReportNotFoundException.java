package com.gume.sonar.domain.exception;

import java.util.UUID;

public class ReportNotFoundException extends RuntimeException {
    
    public ReportNotFoundException(UUID id) {
        super("Report not found with ID: " + id);
    }
}
