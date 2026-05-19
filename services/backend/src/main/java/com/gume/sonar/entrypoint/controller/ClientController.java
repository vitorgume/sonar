package com.gume.sonar.entrypoint.controller;

import com.gume.sonar.application.usecase.ClientUseCase;
import com.gume.sonar.domain.Client;
import com.gume.sonar.entrypoint.controller.dto.ClientDto;
import com.gume.sonar.entrypoint.controller.dto.ResponseDto;
import com.gume.sonar.entrypoint.mapper.ClientDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientUseCase clientUseCase;

    @PostMapping
    public ResponseEntity<ResponseDto<ClientDto>> create(@RequestBody ClientDto clientDto) {
        Client client = ClientDtoMapper.toDomain(clientDto);
        Client createdClient = clientUseCase.create(client);
        ResponseDto<ClientDto> response = new ResponseDto<>(ClientDtoMapper.toDto(createdClient));
        return ResponseEntity.created(
            UriComponentsBuilder.newInstance()
                    .path("/{id}")
                    .buildAndExpand(createdClient != null ? createdClient.getId() : UUID.randomUUID())
                    .toUri()
        ).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<ClientDto>> findById(@PathVariable UUID id) {
        Client client = clientUseCase.findById(id);
        ResponseDto<ClientDto> response = new ResponseDto<>(ClientDtoMapper.toDto(client));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<ClientDto>>> findAll() {
        List<Client> clients = clientUseCase.findAll();
        ResponseDto<List<ClientDto>> response = new ResponseDto<>(clients.stream()
                .map(ClientDtoMapper::toDto)
                .collect(Collectors.toList()));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<ClientDto>> update(@PathVariable UUID id, @RequestBody ClientDto clientDto) {
        Client client = ClientDtoMapper.toDomain(clientDto);
        Client updatedClient = clientUseCase.update(id, client);
        ResponseDto<ClientDto> response = new ResponseDto<>(ClientDtoMapper.toDto(updatedClient));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        clientUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
