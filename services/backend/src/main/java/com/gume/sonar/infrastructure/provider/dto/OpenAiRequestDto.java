package com.gume.sonar.infrastructure.provider.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenAiRequestDto {
    private String model;
    private List<OpenAiMessageDto> messages;
    private Double temperature;
}
