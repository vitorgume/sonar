package com.gume.sonar.entrypoint.mapper;

import com.gume.sonar.domain.LoginResponse;
import com.gume.sonar.entrypoint.controller.dto.LoginResponseDto;

public class LoginResponseDtoMapper {

    public static LoginResponseDto toDto(LoginResponse domain) {
        if (domain == null) {
            return null;
        }

        return LoginResponseDto.builder()
                .userId(domain.getUserId())
                .name(domain.getName())
                .token(domain.getToken())
                .build();
    }

    public static LoginResponse toDomain(LoginResponseDto dto) {
        if (dto == null) {
            return null;
        }

        return LoginResponse.builder()
                .userId(dto.getUserId())
                .name(dto.getName())
                .token(dto.getToken())
                .build();
    }
}
