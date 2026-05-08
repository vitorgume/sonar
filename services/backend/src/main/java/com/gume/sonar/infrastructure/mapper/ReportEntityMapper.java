package com.gume.sonar.infrastructure.mapper;

import com.gume.sonar.domain.Report;
import com.gume.sonar.infrastructure.repository.entity.ReportEntity;

public class ReportEntityMapper {

    public static ReportEntity toEntity(Report domain) {
        if (domain == null) {
            return null;
        }

        return ReportEntity.builder()
                .id(domain.getId())
                .title(domain.getTitle())
                .user(UserEntityMapper.toEntity(domain.getUser()))
                .client(domain.getClient())
                .analysis(domain.getAnalysis())
                .transcript(domain.getTranscript())
                .creationDate(domain.getCreationDate())
                .build();
    }

    public static Report toDomain(ReportEntity entity) {
        if (entity == null) {
            return null;
        }

        return Report.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .user(UserEntityMapper.toDomain(entity.getUser()))
                .client(entity.getClient())
                .analysis(entity.getAnalysis())
                .transcript(entity.getTranscript())
                .creationDate(entity.getCreationDate())
                .build();
    }
}
