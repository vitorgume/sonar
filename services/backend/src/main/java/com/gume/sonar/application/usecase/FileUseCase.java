package com.gume.sonar.application.usecase;

import com.gume.sonar.application.gateway.FileGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileUseCase {

    private final FileGateway fileGateway;

    public String generateUploadUrl(String fileKey, String contentType) {
        return fileGateway.generateUploadUrl(fileKey, contentType);
    }
}