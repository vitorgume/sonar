package com.gume.sonar.infrastructure.provider.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnvioTranscricaoRequestDto {
    @JsonProperty("audio_url")
    private String audioUrl;
    
    @JsonProperty("language_code")
    private String languageCode;
}
