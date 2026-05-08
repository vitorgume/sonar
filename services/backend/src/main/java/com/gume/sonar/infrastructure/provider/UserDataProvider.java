package com.gume.sonar.infrastructure.provider;

import com.gume.sonar.application.gateway.UserGateway;
import com.gume.sonar.domain.User;
import com.gume.sonar.infrastructure.mapper.UserEntityMapper;
import com.gume.sonar.infrastructure.repository.UserRepository;
import com.gume.sonar.infrastructure.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserDataProvider implements UserGateway {

    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        UserEntity entity = UserEntityMapper.toEntity(user);
        UserEntity savedEntity = userRepository.save(entity);
        return UserEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id)
                .map(UserEntityMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll().stream()
                .map(UserEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }
}
