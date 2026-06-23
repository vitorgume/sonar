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

    public Prompt create(UUID userId, Prompt prompt) {
        if (prompt.getLastUpdate() == null) {
            prompt.setLastUpdate(LocalDateTime.now());
        }

        prompt.setUser(User.builder().id(userId).build());
        return promptGateway.save(prompt);
    }

    public Prompt findById(UUID userId, UUID id) {
        return promptGateway.findById(userId, id)
                .orElseThrow(() -> new PromptNotFoundException(id));
    }

    public List<Prompt> findAll(UUID userId) {
        return promptGateway.findAll(userId);
    }

    public Prompt update(UUID userId, UUID id, Prompt prompt) {
        Prompt existingPrompt = findById(userId, id);
        
        existingPrompt.setTitle(prompt.getTitle());
        existingPrompt.setContent(prompt.getContent());
        existingPrompt.setLastUpdate(LocalDateTime.now());
        
        return promptGateway.save(existingPrompt);
    }
}
