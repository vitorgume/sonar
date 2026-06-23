package com.gume.sonar.entrypoint.controller;

import com.gume.sonar.application.usecase.FileUseCase;
import com.gume.sonar.entrypoint.controller.dto.ResponseDto;
import com.gume.sonar.entrypoint.controller.dto.UploadUrlRequestDto;
import com.gume.sonar.entrypoint.controller.dto.UploadUrlResponseDto;
import com.gume.sonar.entrypoint.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileUseCase fileUseCase;
    private final CurrentUser currentUser;

    @PostMapping("/presigned-url")
    public ResponseEntity<ResponseDto<UploadUrlResponseDto>> generateUploadUrl(@RequestBody UploadUrlRequestDto request) {
        String userPrefix = currentUser.getId() == null ? "anonymous" : currentUser.getId().toString();
        String fileKey = userPrefix + "/" + UUID.randomUUID() + "-" + request.getFileName().replaceAll("[^a-zA-Z0-9.-]", "_");
        String uploadUrl = fileUseCase.generateUploadUrl(fileKey, request.getContentType());
        
        UploadUrlResponseDto responseData = new UploadUrlResponseDto(uploadUrl, fileKey);
        ResponseDto<UploadUrlResponseDto> response = new ResponseDto<>(responseData);
        
        return ResponseEntity.ok(response);
    }
}
