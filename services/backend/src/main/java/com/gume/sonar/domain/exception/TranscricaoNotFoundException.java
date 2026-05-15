package com.gume.sonar.domain.exception;

import java.util.UUID;

public class TranscricaoNotFoundException extends RuntimeException {
    
    public TranscricaoNotFoundException(UUID id) {
        super("Transcricao not found with ID: " + id);
    }
}
