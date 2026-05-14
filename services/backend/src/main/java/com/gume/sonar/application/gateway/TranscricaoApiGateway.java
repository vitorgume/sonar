package com.gume.sonar.application.gateway;

import com.gume.sonar.infrastructure.provider.dto.BuscaTranscricaoResponseDto;

import java.util.UUID;

public interface TranscricaoApiGateway {
    
    UUID enviarTranscricao(String audioUrl);
    
    BuscaTranscricaoResponseDto buscarTranscricaoCompleta(UUID transcriptionId);
}
