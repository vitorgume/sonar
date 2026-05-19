package com.gume.sonar.domain.exception;

import java.util.UUID;

public class ClientNotFoundException extends RuntimeException {
    
    public ClientNotFoundException(UUID id) {
        super("Client not found with ID: " + id);
    }
}
