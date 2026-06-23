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

    public Client create(UUID userId, Client client) {
        if (client.getCreationDate() == null) {
            client.setCreationDate(LocalDateTime.now());
        }

        client.setUser(User.builder().id(userId).build());
        return clientGateway.save(client);
    }

    public Client findById(UUID userId, UUID id) {
        return clientGateway.findById(userId, id)
                .orElseThrow(() -> new ClientNotFoundException(id));
    }

    public List<Client> findAll(UUID userId) {
        return clientGateway.findAll(userId);
    }

    public Client update(UUID userId, UUID id, Client client) {
        Client existingClient = findById(userId, id);
        
        existingClient.setName(client.getName());
        
        return clientGateway.save(existingClient);
    }

    public void delete(UUID userId, UUID id) {
        Client existingClient = findById(userId, id);
        clientGateway.deleteById(userId, existingClient.getId());
    }
}
