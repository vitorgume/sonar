package com.gume.sonar.application.usecase;

import com.gume.sonar.application.gateway.PromptGateway;
import com.gume.sonar.domain.Client;
import com.gume.sonar.domain.Prompt;
import com.gume.sonar.domain.User;
import com.gume.sonar.domain.exception.PromptAlreadyExistsForClientException;
import com.gume.sonar.domain.exception.PromptClientRequiredException;
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
    private final ClientUseCase clientUseCase;

    public Prompt create(Prompt prompt, User authenticatedUser) {
        Client client = resolveClient(prompt, authenticatedUser);
        promptGateway.findByClientIdAndUserId(client.getId(), authenticatedUser.getId())
                .ifPresent(existingPrompt -> {
                    throw new PromptAlreadyExistsForClientException(client.getId());
                });

        prompt.setClient(client);
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

    public Prompt findByClientIdAndUserId(UUID clientId, UUID userId) {
        return promptGateway.findByClientIdAndUserId(clientId, userId)
                .orElseThrow(() -> new PromptNotFoundException("Prompt not found for client ID: " + clientId));
    }

    public Prompt update(UUID id, Prompt prompt, User authenticatedUser) {
        Prompt existingPrompt = findByIdAndUserId(id, authenticatedUser.getId());
        Client client = resolveClient(prompt, authenticatedUser);

        promptGateway.findByClientIdAndUserId(client.getId(), authenticatedUser.getId())
                .filter(promptByClient -> !promptByClient.getId().equals(existingPrompt.getId()))
                .ifPresent(promptByClient -> {
                    throw new PromptAlreadyExistsForClientException(client.getId());
                });

        existingPrompt.setTitle(prompt.getTitle());
        existingPrompt.setContent(prompt.getContent());
        existingPrompt.setClient(client);
        existingPrompt.setLastUpdate(LocalDateTime.now());

        return promptGateway.save(existingPrompt);
    }

    private Client resolveClient(Prompt prompt, User authenticatedUser) {
        if (prompt.getClient() == null || prompt.getClient().getId() == null) {
            throw new PromptClientRequiredException();
        }

        return clientUseCase.findByIdAndUserId(prompt.getClient().getId(), authenticatedUser.getId());
    }
}
