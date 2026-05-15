package com.gume.sonar.application.usecase;

import com.gume.sonar.application.gateway.TranscricaoApiGateway;
import com.gume.sonar.infrastructure.provider.dto.BuscaTranscricaoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TranscricaoApiUseCase {

    private final TranscricaoApiGateway transcricaoApiGateway;

    public UUID enviarTranscricao(String audioUrl) {
        return transcricaoApiGateway.enviarTranscricao(audioUrl);
    }

    public BuscaTranscricaoResponseDto buscarTranscricaoCompleta(UUID transcriptionId) {
        return transcricaoApiGateway.buscarTranscricaoCompleta(transcriptionId);
    }
}
