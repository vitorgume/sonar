package com.gume.sonar.infrastructure.provider.exception;

public class AgenteIAApiException extends RuntimeException {
    
    public AgenteIAApiException(String message) {
        super(message);
    }

    public AgenteIAApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
