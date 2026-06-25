package com.gume.sonar.application.gateway;

import com.gume.sonar.domain.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientGateway {
    
    Client save(Client client);
    
    Optional<Client> findById(UUID id);

    Optional<Client> findByIdAndUserId(UUID id, UUID userId);
    
    List<Client> findAll();

    List<Client> findAllByUserId(UUID userId);
    
    void deleteById(UUID id);
}
