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
                .user(UserDtoMapper.toDto(domain.getUser()))
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
                .user(null)
                .lastUpdate(dto.getLastUpdate())
                .build();
    }
}
