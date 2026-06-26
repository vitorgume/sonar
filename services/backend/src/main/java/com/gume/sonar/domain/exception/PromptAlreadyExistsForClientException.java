package com.gume.sonar.domain.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PromptAlreadyExistsForClientException extends RuntimeException {

    public PromptAlreadyExistsForClientException(UUID clientId) {
        super("Prompt already exists for client ID: " + clientId);
    }
}
