package com.gume.sonar.entrypoint.controller;

import com.gume.sonar.application.usecase.PromptUseCase;
import com.gume.sonar.domain.Prompt;
import com.gume.sonar.entrypoint.controller.dto.PromptDto;
import com.gume.sonar.entrypoint.controller.dto.ResponseDto;
import com.gume.sonar.entrypoint.mapper.PromptDtoMapper;
import com.gume.sonar.entrypoint.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/prompts")
@RequiredArgsConstructor
public class PromptController {

    private final PromptUseCase promptUseCase;
    private final CurrentUser currentUser;

    @PostMapping
    public ResponseEntity<ResponseDto<PromptDto>> create(@RequestBody PromptDto promptDto) {
        UUID userId = currentUser.getId();
        Prompt prompt = PromptDtoMapper.toDomain(promptDto);
        Prompt createdPrompt = promptUseCase.create(userId, prompt);
        ResponseDto<PromptDto> response = new ResponseDto<>(PromptDtoMapper.toDto(createdPrompt));
        return ResponseEntity.created(
            UriComponentsBuilder.newInstance()
                    .path("/{id}")
                    .buildAndExpand(createdPrompt != null ? createdPrompt.getId() : UUID.randomUUID())
                    .toUri()
        ).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<PromptDto>> findById(@PathVariable UUID id) {
        UUID userId = currentUser.getId();
        Prompt prompt = promptUseCase.findById(userId, id);
        ResponseDto<PromptDto> response = new ResponseDto<>(PromptDtoMapper.toDto(prompt));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<PromptDto>>> findAll() {
        UUID userId = currentUser.getId();
        List<Prompt> prompts = promptUseCase.findAll(userId);
        ResponseDto<List<PromptDto>> response = new ResponseDto<>(
                prompts == null ? List.of() : prompts.stream()
                        .map(PromptDtoMapper::toDto)
                        .collect(Collectors.toList())
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<PromptDto>> update(@PathVariable UUID id, @RequestBody PromptDto promptDto) {
        UUID userId = currentUser.getId();
        Prompt prompt = PromptDtoMapper.toDomain(promptDto);
        Prompt updatedPrompt = promptUseCase.update(userId, id, prompt);
        ResponseDto<PromptDto> response = new ResponseDto<>(PromptDtoMapper.toDto(updatedPrompt));
        return ResponseEntity.ok(response);
    }
}
