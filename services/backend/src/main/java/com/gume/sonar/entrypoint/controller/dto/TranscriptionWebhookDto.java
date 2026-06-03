package com.gume.sonar.entrypoint.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TranscriptionWebhookDto {

    @JsonProperty("transcript_id")
    private UUID transcriptId;

    private String status;
}
