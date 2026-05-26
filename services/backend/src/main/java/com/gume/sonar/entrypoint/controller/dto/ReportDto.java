package com.gume.sonar.entrypoint.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.gume.sonar.domain.enums.ReportStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {
    private UUID id;
    private String title;
    private UserDto user;
    private ClientDto client;
    private String analysis;
    private String transcript;
    private String audioFileKey;
    private LocalDateTime creationDate;
    private ReportStatus status;
}
