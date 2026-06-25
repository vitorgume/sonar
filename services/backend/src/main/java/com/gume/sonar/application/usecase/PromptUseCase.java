package com.gume.sonar.application.usecase;

import com.gume.sonar.application.gateway.PromptGateway;
import com.gume.sonar.domain.Prompt;
import com.gume.sonar.domain.User;
import com.gume.sonar.domain.exception.PromptNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PromptUseCase {

    private final PromptGateway promptGateway;

    public Prompt create(Prompt prompt, User authenticatedUser) {
        prompt.setUser(authenticatedUser);
        if (prompt.getLastUpdate() == null) {
            prompt.setLastUpdate(LocalDateTime.now());
        }
        return promptGateway.save(prompt);
    }

    public Prompt findById(UUID id) {
        return promptGateway.findById(id)
                .orElseThrow(() -> new PromptNotFoundException(id));
    }

    public List<Prompt> findAll() {
        return promptGateway.findAll();
    }

    public Prompt findByIdAndUserId(UUID id, UUID userId) {
        return promptGateway.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new PromptNotFoundException(id));
    }

    public List<Prompt> findAllByUserId(UUID userId) {
        return promptGateway.findAllByUserId(userId);
    }

    public Prompt update(UUID id, Prompt prompt, User authenticatedUser) {
        Prompt existingPrompt = findByIdAndUserId(id, authenticatedUser.getId());
        
        existingPrompt.setTitle(prompt.getTitle());
        existingPrompt.setContent(prompt.getContent());
        existingPrompt.setUser(authenticatedUser);
        existingPrompt.setLastUpdate(LocalDateTime.now());
        
        return promptGateway.save(existingPrompt);
    }
}
