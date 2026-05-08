package com.gume.sonar.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    private UUID id;
    private String title;
    private User user;
    private String client;
    private String analysis;
    private String transcript;
    private LocalDateTime creationDate;
}
