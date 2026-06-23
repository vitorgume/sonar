package com.gume.sonar.infrastructure.provider;

import com.gume.sonar.application.gateway.ClientGateway;
import com.gume.sonar.domain.Client;
import com.gume.sonar.infrastructure.mapper.ClientEntityMapper;
import com.gume.sonar.infrastructure.repository.ClientRepository;
import com.gume.sonar.infrastructure.repository.entity.ClientEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ClientDataProvider implements ClientGateway {

    private final ClientRepository clientRepository;

    @Override
    public Client save(Client client) {
        ClientEntity entity = ClientEntityMapper.toEntity(client);
        ClientEntity savedEntity = clientRepository.save(entity);
        return ClientEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Client> findById(UUID userId, UUID id) {
        return clientRepository.findByIdAndUser_Id(id, userId)
                .map(ClientEntityMapper::toDomain);
    }

    @Override
    public List<Client> findAll(UUID userId) {
        return clientRepository.findAllByUser_Id(userId).stream()
                .map(ClientEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID userId, UUID id) {
        clientRepository.deleteByIdAndUser_Id(id, userId);
    }
}
