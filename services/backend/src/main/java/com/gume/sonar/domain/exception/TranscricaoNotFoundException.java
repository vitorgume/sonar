package com.gume.sonar.domain.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TranscricaoNotFoundException extends RuntimeException {
    
    public TranscricaoNotFoundException(UUID id) {
        super("Transcricao not found with ID: " + id);
    }
}
