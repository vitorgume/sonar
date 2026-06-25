package com.gume.sonar.infrastructure.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "prompts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromptEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    private String title;
    
    private String content;
    
    @OneToOne
    @JoinColumn(name = "client_id", unique = true)
    private ClientEntity client;
    
    private LocalDateTime lastUpdate;
}
