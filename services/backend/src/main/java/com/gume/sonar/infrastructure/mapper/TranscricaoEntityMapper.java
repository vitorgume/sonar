package com.gume.sonar.infrastructure.mapper;

import com.gume.sonar.domain.Transcricao;
import com.gume.sonar.infrastructure.repository.entity.TranscricaoEntity;

public class TranscricaoEntityMapper {

    public static TranscricaoEntity toEntity(Transcricao domain) {
        if (domain == null) {
            return null;
        }

        return TranscricaoEntity.builder()
                .id(domain.getId())
                .urlAudio(domain.getUrlAudio())
                .creationDate(domain.getCreationDate())
                .build();
    }

    public static Transcricao toDomain(TranscricaoEntity entity) {
        if (entity == null) {
            return null;
        }

        return Transcricao.builder()
                .id(entity.getId())
                .urlAudio(entity.getUrlAudio())
                .creationDate(entity.getCreationDate())
                .build();
    }
}
