package com.gume.sonar.application.gateway;

import com.gume.sonar.domain.Prompt;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PromptGateway {
    
    Prompt save(Prompt prompt);
    
    Optional<Prompt> findById(UUID userId, UUID id);
    
    List<Prompt> findAll(UUID userId);
}
