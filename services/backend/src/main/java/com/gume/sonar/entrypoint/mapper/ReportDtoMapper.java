package com.gume.sonar.entrypoint.mapper;

import com.gume.sonar.domain.Report;
import com.gume.sonar.entrypoint.controller.dto.ReportDto;

public class ReportDtoMapper {

    public static ReportDto toDto(Report domain) {
        if (domain == null) {
            return null;
        }

        return ReportDto.builder()
                .id(domain.getId())
                .title(domain.getTitle())
                .user(UserDtoMapper.toDto(domain.getUser()))
                .client(ClientDtoMapper.toDto(domain.getClient()))
                .analysis(domain.getAnalysis())
                .transcript(domain.getTranscript())
                .creationDate(domain.getCreationDate())
                .status(domain.getStatus())
                .build();
    }

    public static Report toDomain(ReportDto dto) {
        if (dto == null) {
            return null;
        }

        return Report.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .user(UserDtoMapper.toDomain(dto.getUser()))
                .client(ClientDtoMapper.toDomain(dto.getClient()))
                .analysis(dto.getAnalysis())
                .transcript(dto.getTranscript())
                .creationDate(dto.getCreationDate())
                .status(dto.getStatus())
                .build();
    }
}
