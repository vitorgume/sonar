package com.gume.sonar.entrypoint.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromptDto {
    private UUID id;
    private String title;
    private String content;
    private UserDto user;
    private LocalDateTime lastUpdate;
}
