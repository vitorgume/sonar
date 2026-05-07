package com.gume.sonar.infrastructure.mapper;

import com.gume.sonar.domain.User;
import com.gume.sonar.infrastructure.repository.entity.UserEntity;

public class UserEntityMapper {

    public static UserEntity toEntity(User domain) {
        if (domain == null) {
            return null;
        }

        return UserEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .email(domain.getEmail())
                .password(domain.getPassword())
                .build();
    }

    public static User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        return User.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .build();
    }
}
