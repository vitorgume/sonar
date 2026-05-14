package com.gume.sonar.infrastructure.provider.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnvioTranscricaoResponseDto {
    private UUID id;
    private String status;
    @JsonProperty("audio_url")
    private String audioUrl;
    @JsonProperty("language_code")
    private String languageCode;
    private String text;
}
