package com.gume.sonar.entrypoint.mapper;

import com.gume.sonar.domain.User;
import com.gume.sonar.entrypoint.controller.dto.UserDto;

public class UserDtoMapper {

    public static UserDto toDto(User domain) {
        if (domain == null) {
            return null;
        }

        return UserDto.builder()
                .id(domain.getId())
                .name(domain.getName())
                .email(domain.getEmail())
                .password(domain.getPassword())
                .build();
    }

    public static User toDomain(UserDto dto) {
        if (dto == null) {
            return null;
        }

        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
    }
}
