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
                .email(domain.getEmail())
                .token(domain.getToken())
                .build();
    }

    public static LoginResponse toDomain(LoginResponseDto dto) {
        if (dto == null) {
            return null;
        }

        return LoginResponse.builder()
                .userId(dto.getUserId())
                .email(dto.getEmail())
                .token(dto.getToken())
                .build();
    }
}
