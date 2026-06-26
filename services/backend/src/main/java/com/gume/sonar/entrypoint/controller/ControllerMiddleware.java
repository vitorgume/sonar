package com.gume.sonar.entrypoint.controller;

import com.gume.sonar.domain.exception.ClientNotFoundException;
import com.gume.sonar.domain.exception.InvalidCredentialsException;
import com.gume.sonar.domain.exception.PromptAlreadyExistsForClientException;
import com.gume.sonar.domain.exception.PromptClientRequiredException;
import com.gume.sonar.domain.exception.PromptNotFoundException;
import com.gume.sonar.domain.exception.ReportNotFoundException;
import com.gume.sonar.domain.exception.TranscricaoNotFoundException;
import com.gume.sonar.domain.exception.UserNotFoundException;
import com.gume.sonar.entrypoint.controller.dto.ResponseDto;
import com.gume.sonar.infrastructure.provider.exception.AgenteIAApiException;
import com.gume.sonar.infrastructure.provider.exception.TranscricaoApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class ControllerMiddleware {

    @ExceptionHandler({
            ClientNotFoundException.class,
            PromptNotFoundException.class,
            ReportNotFoundException.class,
            TranscricaoNotFoundException.class,
            UserNotFoundException.class
    })
    public ResponseEntity<ResponseDto<Void>> handleNotFound(RuntimeException exception) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ResponseDto<Void>> handleUnauthorized(InvalidCredentialsException exception) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler({
            PromptClientRequiredException.class,
            PromptAlreadyExistsForClientException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ResponseDto<Void>> handleBadRequest(RuntimeException exception) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler({
            AgenteIAApiException.class,
            TranscricaoApiException.class
    })
    public ResponseEntity<ResponseDto<Void>> handleInternalServerError(RuntimeException exception) {
        log.error("Erro de infraestrutura ao processar requisicao", exception);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<Void>> handleUnexpectedError(Exception exception) {
        log.error("Erro inesperado ao processar requisicao", exception);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor.");
    }

    private ResponseEntity<ResponseDto<Void>> buildErrorResponse(HttpStatus status, String message) {
        ResponseDto.ErroDto error = ResponseDto.ErroDto.builder()
                .mensagens(List.of(resolveMessage(message)))
                .build();

        return ResponseEntity.status(status)
                .body(ResponseDto.comErro(error));
    }

    private String resolveMessage(String message) {
        return message == null || message.isBlank() ? "Erro ao processar requisicao." : message;
    }
}
