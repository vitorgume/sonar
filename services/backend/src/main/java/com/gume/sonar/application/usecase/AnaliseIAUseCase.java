package com.gume.sonar.application.usecase;

import com.gume.sonar.application.gateway.AnaliseIAGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnaliseIAUseCase {

    private final AnaliseIAGateway analiseIAGateway;

    public String analisarTranscricao(String prompt, String transcricao) {
        return analiseIAGateway.analisarTranscricao(prompt, transcricao);
    }
}
