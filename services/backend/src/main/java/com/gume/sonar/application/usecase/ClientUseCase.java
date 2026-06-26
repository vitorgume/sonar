package com.gume.sonar.application.usecase;

import com.gume.sonar.application.gateway.ClientGateway;
import com.gume.sonar.domain.Client;
import com.gume.sonar.domain.User;
import com.gume.sonar.domain.exception.ClientNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientUseCase {

    private final ClientGateway clientGateway;

    public Client create(Client client, User authenticatedUser) {
        client.setUser(authenticatedUser);
        if (client.getCreationDate() == null) {
            client.setCreationDate(LocalDateTime.now());
        }
        return clientGateway.save(client);
    }

    public Client findById(UUID id) {
        return clientGateway.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
    }

    public List<Client> findAll() {
        return clientGateway.findAll();
    }

    public Client findByIdAndUserId(UUID id, UUID userId) {
        return clientGateway.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ClientNotFoundException(id));
    }

    public List<Client> findAllByUserId(UUID userId) {
        return clientGateway.findAllByUserId(userId);
    }

    public Client update(UUID id, Client client, User authenticatedUser) {
        Client existingClient = findByIdAndUserId(id, authenticatedUser.getId());
        
        existingClient.setName(client.getName());
        existingClient.setUser(authenticatedUser);
        
        return clientGateway.save(existingClient);
    }

    public void delete(UUID id, UUID userId) {
        Client existingClient = findByIdAndUserId(id, userId);
        clientGateway.deleteById(existingClient.getId());
    }
}
