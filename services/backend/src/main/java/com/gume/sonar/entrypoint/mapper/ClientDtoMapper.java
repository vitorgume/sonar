package com.gume.sonar.entrypoint.mapper;

import com.gume.sonar.domain.Client;
import com.gume.sonar.entrypoint.controller.dto.ClientDto;

public class ClientDtoMapper {

    public static ClientDto toDto(Client domain) {
        if (domain == null) {
            return null;
        }

        return ClientDto.builder()
                .id(domain.getId())
                .name(domain.getName())
                .user(UserDtoMapper.toDto(domain.getUser()))
                .creationDate(domain.getCreationDate())
                .build();
    }

    public static Client toDomain(ClientDto dto) {
        if (dto == null) {
            return null;
        }

        return Client.builder()
                .id(dto.getId())
                .name(dto.getName())
                .user(null)
                .creationDate(dto.getCreationDate())
                .build();
    }
}
