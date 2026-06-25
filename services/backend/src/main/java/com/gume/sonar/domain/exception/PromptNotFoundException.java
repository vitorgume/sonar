package com.gume.sonar.domain.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PromptNotFoundException extends RuntimeException {
    
    public PromptNotFoundException(UUID id) {
        super("Prompt not found with ID: " + id);
    }
}
