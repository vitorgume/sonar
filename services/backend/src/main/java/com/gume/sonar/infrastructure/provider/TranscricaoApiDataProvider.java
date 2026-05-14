package com.gume.sonar.infrastructure.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gume.sonar.application.gateway.TranscricaoApiGateway;
import com.gume.sonar.infrastructure.provider.dto.BuscaTranscricaoResponseDto;
import com.gume.sonar.infrastructure.provider.dto.EnvioTranscricaoRequestDto;
import com.gume.sonar.infrastructure.provider.dto.EnvioTranscricaoResponseDto;
import com.gume.sonar.infrastructure.provider.exception.TranscricaoApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TranscricaoApiDataProvider implements TranscricaoApiGateway {

    private final ObjectMapper objectMapper;
    
    @Value("${assemblyai.api.key:default_api_key}")
    private String apiKey;

    private static final String API_URL = "https://api.assemblyai.com/v2/transcript";
    private static final int MAX_RETRIES = 3;

    @Override
    public UUID enviarTranscricao(String audioUrl) {
        EnvioTranscricaoRequestDto requestDto = EnvioTranscricaoRequestDto.builder()
                .audioUrl(audioUrl)
                .languageCode("pt")
                .build();

        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            String requestBody = objectMapper.writeValueAsString(requestDto);

            for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
                try {
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(API_URL))
                            .header("Authorization", apiKey)
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                            .build();

                    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                    if (response.statusCode() >= 200 && response.statusCode() < 300) {
                        EnvioTranscricaoResponseDto responseDto = objectMapper.readValue(response.body(), EnvioTranscricaoResponseDto.class);
                        return responseDto.getId();
                    } else if (attempt == MAX_RETRIES) {
                        throw new TranscricaoApiException("Falha ao enviar transcrição após " + MAX_RETRIES + " tentativas. HTTP Status: " + response.statusCode() + " Body: " + response.body());
                    }
                } catch (Exception e) {
                    if (attempt == MAX_RETRIES) {
                        throw new TranscricaoApiException("Erro de comunicação com a API do AssemblyAI ao enviar transcrição", e);
                    }
                    sleep(attempt);
                }
            }
        } catch (Exception e) {
            throw new TranscricaoApiException("Erro inesperado ao processar a requisição de transcrição", e);
        }
        
        throw new TranscricaoApiException("Erro inesperado em enviarTranscricao");
    }

    @Override
    public BuscaTranscricaoResponseDto buscarTranscricaoCompleta(UUID transcriptionId) {
        String url = API_URL + "/" + transcriptionId.toString();

        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
                try {
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(url))
                            .header("Authorization", apiKey)
                            .GET()
                            .build();

                    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                    if (response.statusCode() >= 200 && response.statusCode() < 300) {
                        return objectMapper.readValue(response.body(), BuscaTranscricaoResponseDto.class);
                    } else if (attempt == MAX_RETRIES) {
                        throw new TranscricaoApiException("Falha ao buscar transcrição após " + MAX_RETRIES + " tentativas. HTTP Status: " + response.statusCode() + " Body: " + response.body());
                    }
                } catch (Exception e) {
                    if (attempt == MAX_RETRIES) {
                        throw new TranscricaoApiException("Erro de comunicação com a API do AssemblyAI ao buscar transcrição", e);
                    }
                    sleep(attempt);
                }
            }
        } catch (Exception e) {
            throw new TranscricaoApiException("Erro inesperado ao processar a busca da transcrição", e);
        }
        
        throw new TranscricaoApiException("Erro inesperado em buscarTranscricaoCompleta");
    }

    private void sleep(int attempt) {
        try {
            Thread.sleep((long) Math.pow(2, attempt) * 1000); // Backoff exponencial: 2s, 4s, 8s...
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
