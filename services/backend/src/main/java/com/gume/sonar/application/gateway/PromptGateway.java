package com.gume.sonar.application.gateway;

import com.gume.sonar.domain.Prompt;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PromptGateway {
    
    Prompt save(Prompt prompt);
    
    Optional<Prompt> findById(UUID id);

    Optional<Prompt> findByIdAndUserId(UUID id, UUID userId);
    
    List<Prompt> findAll();

    List<Prompt> findAllByUserId(UUID userId);
}
