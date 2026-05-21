package com.gume.sonar.application.usecase;

import com.gume.sonar.application.gateway.ClientGateway;
import com.gume.sonar.domain.Client;
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

    public Client create(Client client) {
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

    public Client update(UUID id, Client client) {
        Client existingClient = findById(id);
        
        existingClient.setName(client.getName());
        existingClient.setUser(client.getUser());
        
        return clientGateway.save(existingClient);
    }

    public void delete(UUID id) {
        Client existingClient = findById(id);
        clientGateway.deleteById(existingClient.getId());
    }
}
