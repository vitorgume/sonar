package com.gume.sonar.domain.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReportNotFoundException extends RuntimeException {
    
    public ReportNotFoundException(UUID id) {
        super("Report not found with ID: " + id);
    }
}
