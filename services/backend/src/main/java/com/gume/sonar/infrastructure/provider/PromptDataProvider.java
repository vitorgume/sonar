package com.gume.sonar.infrastructure.provider;

import com.gume.sonar.application.gateway.PromptGateway;
import com.gume.sonar.domain.Prompt;
import com.gume.sonar.infrastructure.mapper.PromptEntityMapper;
import com.gume.sonar.infrastructure.repository.PromptRepository;
import com.gume.sonar.infrastructure.repository.entity.PromptEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PromptDataProvider implements PromptGateway {

    private final PromptRepository promptRepository;

    @Override
    public Prompt save(Prompt prompt) {
        PromptEntity entity = PromptEntityMapper.toEntity(prompt);
        PromptEntity savedEntity = promptRepository.save(entity);
        return PromptEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Prompt> findById(UUID id) {
        return promptRepository.findById(id)
                .map(PromptEntityMapper::toDomain);
    }

    @Override
    public List<Prompt> findAll() {
        return promptRepository.findAll().stream()
                .map(PromptEntityMapper::toDomain)
                .collect(Collectors.toList());
    }
}
