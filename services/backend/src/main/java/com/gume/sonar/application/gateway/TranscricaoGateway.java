package com.gume.sonar.application.gateway;

import com.gume.sonar.domain.Transcricao;

import java.util.Optional;
import java.util.UUID;

public interface TranscricaoGateway {
    
    Transcricao save(Transcricao transcricao);
    
    Optional<Transcricao> findById(UUID id);
    
    void deleteById(UUID id);
}
