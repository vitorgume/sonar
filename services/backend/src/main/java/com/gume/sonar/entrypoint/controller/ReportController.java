    package com.gume.sonar.entrypoint.controller;

import com.gume.sonar.application.usecase.ReportUseCase;
import com.gume.sonar.domain.Report;
import com.gume.sonar.domain.User;
import com.gume.sonar.entrypoint.controller.dto.ResponseDto;
import com.gume.sonar.entrypoint.controller.dto.ReportDto;
import com.gume.sonar.entrypoint.controller.dto.TranscriptionWebhookDto;
import com.gume.sonar.entrypoint.controller.support.AuthenticatedUserResolver;
import com.gume.sonar.entrypoint.mapper.ReportDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportUseCase reportUseCase;
    private final AuthenticatedUserResolver authenticatedUserResolver;

    @PostMapping
    public ResponseEntity<ResponseDto<ReportDto>> create(@RequestBody ReportDto reportDto, Authentication authentication) {
        User authenticatedUser = authenticatedUserResolver.resolve(authentication);
        Report report = ReportDtoMapper.toDomain(reportDto);
        Report createdReport = reportUseCase.create(report, authenticatedUser);
        ResponseDto<ReportDto> response = new ResponseDto<>(ReportDtoMapper.toDto(createdReport));
        return ResponseEntity.created(
            UriComponentsBuilder.newInstance()
                    .path("/{id}")
                    .buildAndExpand(createdReport.getId())
                    .toUri()
        ).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<ReportDto>> findById(@PathVariable UUID id, Authentication authentication) {
        User authenticatedUser = authenticatedUserResolver.resolve(authentication);
        Report report = reportUseCase.findByIdAndUserId(id, authenticatedUser.getId());
        ResponseDto<ReportDto> response = new ResponseDto<>(ReportDtoMapper.toDto(report));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<ReportDto>>> findAll(Authentication authentication) {
        User authenticatedUser = authenticatedUserResolver.resolve(authentication);
        List<Report> reports = reportUseCase.findAllByUserId(authenticatedUser.getId());
        ResponseDto<List<ReportDto>> response = new ResponseDto<>(reports.stream()
                .map(ReportDtoMapper::toDto)
                .collect(Collectors.toList()));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<ReportDto>> update(@PathVariable UUID id, @RequestBody ReportDto reportDto, Authentication authentication) {
        User authenticatedUser = authenticatedUserResolver.resolve(authentication);
        Report report = ReportDtoMapper.toDomain(reportDto);
        Report updatedReport = reportUseCase.update(id, report, authenticatedUser);
        ResponseDto<ReportDto> response = new ResponseDto<>(ReportDtoMapper.toDto(updatedReport));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, Authentication authentication) {
        User authenticatedUser = authenticatedUserResolver.resolve(authentication);
        reportUseCase.delete(id, authenticatedUser.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> transcriptionWebhook(
            @RequestBody TranscriptionWebhookDto webhookDto) {
        
        if ("completed".equalsIgnoreCase(webhookDto.getStatus())) {
            reportUseCase.finalizarProcessoCriacaoReport(webhookDto.getTranscriptId());
        }
        
        return ResponseEntity.ok().build();
    }
}
