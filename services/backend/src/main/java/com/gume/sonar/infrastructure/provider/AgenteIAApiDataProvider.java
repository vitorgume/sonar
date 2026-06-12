package com.gume.sonar.infrastructure.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gume.sonar.application.gateway.AnaliseIAGateway;
import com.gume.sonar.infrastructure.provider.dto.OpenAiMessageDto;
import com.gume.sonar.infrastructure.provider.dto.OpenAiRequestDto;
import com.gume.sonar.infrastructure.provider.dto.OpenAiResponseDto;
import com.gume.sonar.infrastructure.provider.exception.AgenteIAApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AgenteIAApiDataProvider implements AnaliseIAGateway {

    private final ObjectMapper objectMapper;

    @Value("${openai.api.key:default_api_key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String API_URL;
    
    private static final String MODELO_CHAT = "gpt-4o";

    @Override
    public String analisarTranscricao(String transcricao) {
        OpenAiMessageDto message = OpenAiMessageDto.builder()
                .role("user")
                .content("Analise a seguinte transcrição: " + transcricao)
                .build();

        OpenAiRequestDto requestDto = OpenAiRequestDto.builder()
                .model(MODELO_CHAT)
                .messages(List.of(message))
                .temperature(0.7)
                .build();

        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            String requestBody = objectMapper.writeValueAsString(requestDto);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                OpenAiResponseDto responseDto = objectMapper.readValue(response.body(), OpenAiResponseDto.class);

                if (responseDto.getChoices() == null || responseDto.getChoices().isEmpty()) {
                    throw new AgenteIAApiException("Resposta vazia da IA.");
                }

                String content = responseDto.getChoices().get(0).getMessage().getContent();

                if (content == null || content.trim().isEmpty()) {
                    throw new AgenteIAApiException("Resposta vazia da IA.");
                }

                return content;
            } else {
                throw new AgenteIAApiException("Erro ao enviar mensagem a IA. HTTP Status: " + response.statusCode() + " Body: " + response.body());
            }
        } catch (Exception e) {
            throw new AgenteIAApiException("Erro ao enviar mensagem a IA.", e);
        }
    }
}
