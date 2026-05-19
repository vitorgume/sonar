package com.gume.sonar.domain.exception;

import java.util.UUID;

public class PromptNotFoundException extends RuntimeException {
    
    public PromptNotFoundException(UUID id) {
        super("Prompt not found with ID: " + id);
    }
}
