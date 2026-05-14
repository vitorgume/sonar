package com.gume.sonar.infrastructure.provider.exception;

public class TranscricaoApiException extends RuntimeException {
    
    public TranscricaoApiException(String message) {
        super(message);
    }

    public TranscricaoApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
