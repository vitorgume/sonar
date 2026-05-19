package com.gume.sonar.infrastructure.mapper;

import com.gume.sonar.domain.Client;
import com.gume.sonar.infrastructure.repository.entity.ClientEntity;

public class ClientEntityMapper {

    public static ClientEntity toEntity(Client domain) {
        if (domain == null) {
            return null;
        }

        return ClientEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .user(UserEntityMapper.toEntity(domain.getUser()))
                .creationDate(domain.getCreationDate())
                .build();
    }

    public static Client toDomain(ClientEntity entity) {
        if (entity == null) {
            return null;
        }

        return Client.builder()
                .id(entity.getId())
                .name(entity.getName())
                .user(UserEntityMapper.toDomain(entity.getUser()))
                .creationDate(entity.getCreationDate())
                .build();
    }
}
