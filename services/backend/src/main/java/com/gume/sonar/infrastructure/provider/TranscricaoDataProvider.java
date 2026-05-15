package com.gume.sonar.infrastructure.provider;

import com.gume.sonar.application.gateway.TranscricaoGateway;
import com.gume.sonar.domain.Transcricao;
import com.gume.sonar.infrastructure.mapper.TranscricaoEntityMapper;
import com.gume.sonar.infrastructure.repository.TranscricaoRepository;
import com.gume.sonar.infrastructure.repository.entity.TranscricaoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TranscricaoDataProvider implements TranscricaoGateway {

    private final TranscricaoRepository transcricaoRepository;

    @Override
    public Transcricao save(Transcricao transcricao) {
        TranscricaoEntity entity = TranscricaoEntityMapper.toEntity(transcricao);
        TranscricaoEntity savedEntity = transcricaoRepository.save(entity);
        return TranscricaoEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Transcricao> findById(UUID id) {
        return transcricaoRepository.findById(id)
                .map(TranscricaoEntityMapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        transcricaoRepository.deleteById(id);
    }
}
