package com.gume.sonar.application.gateway;

public interface FileGateway {
    String generateUploadUrl(String fileKey, String contentType);
}