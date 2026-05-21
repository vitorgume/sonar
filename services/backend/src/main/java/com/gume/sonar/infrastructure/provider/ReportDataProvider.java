package com.gume.sonar.infrastructure.provider;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.gume.sonar.application.gateway.ReportGateway;
import com.gume.sonar.domain.Report;
import com.gume.sonar.infrastructure.mapper.ReportEntityMapper;
import com.gume.sonar.infrastructure.repository.ReportRepository;
import com.gume.sonar.infrastructure.repository.entity.ReportEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReportDataProvider implements ReportGateway {


    private final ReportRepository reportRepository;

    @Override
    public Report save(Report report) {
        ReportEntity entity = ReportEntityMapper.toEntity(report);
        ReportEntity savedEntity = reportRepository.save(entity);
        return ReportEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Report> findById(UUID id) {
        return reportRepository.findById(id)
                .map(ReportEntityMapper::toDomain);
    }

    @Override
    public List<Report> findAll() {
        return reportRepository.findAll().stream()
                .map(ReportEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        reportRepository.deleteById(id);
    }
}
