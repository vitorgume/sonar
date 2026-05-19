package com.gume.sonar.infrastructure.mapper;

import com.gume.sonar.domain.Prompt;
import com.gume.sonar.infrastructure.repository.entity.PromptEntity;

public class PromptEntityMapper {

    public static PromptEntity toEntity(Prompt domain) {
        if (domain == null) {
            return null;
        }

        return PromptEntity.builder()
                .id(domain.getId())
                .title(domain.getTitle())
                .content(domain.getContent())
                .user(UserEntityMapper.toEntity(domain.getUser()))
                .lastUpdate(domain.getLastUpdate())
                .build();
    }

    public static Prompt toDomain(PromptEntity entity) {
        if (entity == null) {
            return null;
        }

        return Prompt.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .user(UserEntityMapper.toDomain(entity.getUser()))
                .lastUpdate(entity.getLastUpdate())
                .build();
    }
}
