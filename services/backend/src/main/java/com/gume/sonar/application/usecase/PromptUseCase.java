package com.gume.sonar.application.usecase;

import com.gume.sonar.application.gateway.PromptGateway;
import com.gume.sonar.domain.Prompt;
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

    public Prompt create(Prompt prompt) {
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

    public Prompt update(UUID id, Prompt prompt) {
        Prompt existingPrompt = findById(id);
        
        existingPrompt.setTitle(prompt.getTitle());
        existingPrompt.setContent(prompt.getContent());
        existingPrompt.setUser(prompt.getUser());
        existingPrompt.setLastUpdate(LocalDateTime.now());
        
        return promptGateway.save(existingPrompt);
    }
}
