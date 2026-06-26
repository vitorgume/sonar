package com.gume.sonar.entrypoint.mapper;

import com.gume.sonar.domain.Prompt;
import com.gume.sonar.entrypoint.controller.dto.PromptDto;

public class PromptDtoMapper {

    public static PromptDto toDto(Prompt domain) {
        if (domain == null) {
            return null;
        }

        return PromptDto.builder()
                .id(domain.getId())
                .title(domain.getTitle())
                .content(domain.getContent())
                .client(ClientDtoMapper.toDto(domain.getClient()))
                .lastUpdate(domain.getLastUpdate())
                .build();
    }

    public static Prompt toDomain(PromptDto dto) {
        if (dto == null) {
            return null;
        }

        return Prompt.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .client(ClientDtoMapper.toDomain(dto.getClient()))
                .lastUpdate(dto.getLastUpdate())
                .build();
    }
}
