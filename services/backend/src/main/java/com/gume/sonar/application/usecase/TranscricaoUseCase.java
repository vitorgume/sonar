package com.gume.sonar.application.usecase;

import com.gume.sonar.application.gateway.TranscricaoGateway;
import com.gume.sonar.domain.Transcricao;
import com.gume.sonar.domain.exception.TranscricaoNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TranscricaoUseCase {

    private final TranscricaoGateway transcricaoGateway;

    public Transcricao create(Transcricao transcricao) {
        if (transcricao.getCreationDate() == null) {
            transcricao.setCreationDate(LocalDateTime.now());
        }
        return transcricaoGateway.save(transcricao);
    }

    public Transcricao findById(UUID id) {
        return transcricaoGateway.findById(id)
                .orElseThrow(() -> new TranscricaoNotFoundException(id));
    }

    public void delete(UUID id) {
        Transcricao transcricao = findById(id);
        transcricaoGateway.deleteById(transcricao.getId());
    }
}
