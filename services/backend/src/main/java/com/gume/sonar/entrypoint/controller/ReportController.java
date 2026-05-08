package com.gume.sonar.entrypoint.controller;

import com.gume.sonar.application.usecase.ReportUseCase;
import com.gume.sonar.domain.Report;
import com.gume.sonar.entrypoint.controller.dto.ResponseDto;
import com.gume.sonar.entrypoint.controller.dto.ReportDto;
import com.gume.sonar.entrypoint.mapper.ReportDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<ResponseDto<ReportDto>> create(@RequestBody ReportDto reportDto) {
        Report report = ReportDtoMapper.toDomain(reportDto);
        Report createdReport = reportUseCase.create(report);
        ResponseDto<ReportDto> response = new ResponseDto<>(ReportDtoMapper.toDto(createdReport));
        return ResponseEntity.created(
            UriComponentsBuilder.newInstance()
                    .path("/{id}")
                    .buildAndExpand(createdReport.getId())
                    .toUri()
        ).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<ReportDto>> findById(@PathVariable UUID id) {
        Report report = reportUseCase.findById(id);
        ResponseDto<ReportDto> response = new ResponseDto<>(ReportDtoMapper.toDto(report));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<ReportDto>>> findAll() {
        List<Report> reports = reportUseCase.findAll();
        ResponseDto<List<ReportDto>> response = new ResponseDto<>(reports.stream()
                .map(ReportDtoMapper::toDto)
                .collect(Collectors.toList()));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<ReportDto>> update(@PathVariable UUID id, @RequestBody ReportDto reportDto) {
        Report report = ReportDtoMapper.toDomain(reportDto);
        Report updatedReport = reportUseCase.update(id, report);
        ResponseDto<ReportDto> response = new ResponseDto<>(ReportDtoMapper.toDto(updatedReport));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        reportUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
