package com.gume.sonar.application.usecase;

import com.gume.sonar.application.gateway.FileGateway;
import com.gume.sonar.application.gateway.ReportGateway;
import com.gume.sonar.domain.Client;
import com.gume.sonar.domain.Report;
import com.gume.sonar.domain.Transcricao;
import com.gume.sonar.domain.User;
import com.gume.sonar.domain.enums.ReportStatus;
import com.gume.sonar.domain.exception.ReportNotFoundException;
import com.gume.sonar.infrastructure.provider.dto.BuscaTranscricaoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportUseCase {

    private final ReportGateway reportGateway;
    private final TranscricaoApiUseCase transcricaoApiUseCase;
    private final TranscricaoUseCase transcricaoUseCase;
    private final AnaliseIAUseCase analiseIAUseCase;
    private final ClientUseCase clientUseCase;
    private final PromptUseCase promptUseCase;
    private final FileGateway fileGateway;

    public Report create(Report report, User authenticatedUser) {
        Client client = clientUseCase.findByIdAndUserId(report.getClient().getId(), authenticatedUser.getId());

        report.setUser(authenticatedUser);
        report.setClient(client);
        if (report.getCreationDate() == null) {
            report.setCreationDate(LocalDateTime.now());
        }

        String urlSeguraParaDownload = fileGateway.generateDownloadUrl(report.getAudioFileKey());

        // 1. Send URL to AssemblyAI
        UUID transcriptionId = transcricaoApiUseCase.enviarTranscricao(urlSeguraParaDownload); // Assuming transcript field temporarily holds the audio URL based on rules
        
        // 2. Save Transcricao domain
        Transcricao transcricao = Transcricao.builder()
                .id(transcriptionId)
                .urlAudio(report.getAudioFileKey()) // <-- Salva só a chave limpa
                .build();
        
        
        // 3. Save Report as PROCESSING
        report.setStatus(ReportStatus.PROCESSING);
        report.setTranscript(null); // Clear URL, we will set the actual text later

        Report reportSalvo = reportGateway.save(report);
    
        transcricao.setReport(reportSalvo);
        transcricaoUseCase.create(transcricao);

        return reportSalvo;
    }
    
    public void finalizarProcessoCriacaoReport(UUID transcriptionId) {
        // 1. Retrieve Transcricao from DB and delete
        Transcricao transcricao = transcricaoUseCase.findById(transcriptionId);
        transcricaoUseCase.delete(transcricao.getId());
        
        // 2. Call AssemblyAI to fetch transcription text
        BuscaTranscricaoResponseDto transcriptionDto = transcricaoApiUseCase.buscarTranscricaoCompleta(transcriptionId);
        
        // 3. Update Report to COMPLETED with text
        Report report = findById(transcricao.getReport().getId());
        report.setTranscript(transcriptionDto.getText());

        // 4. Call AnaliseIA to analyze the text
        String clientPrompt = promptUseCase.findByClientIdAndUserId(
                report.getClient().getId(),
                report.getUser().getId()
        ).getContent();
        String analysis = analiseIAUseCase.analisarTranscricao(clientPrompt, report.getTranscript());
        report.setAnalysis(analysis);

        report.setStatus(ReportStatus.COMPLETED);
        
        reportGateway.save(report);
    }

    public Report findById(UUID id) {
        return reportGateway.findById(id)
                .orElseThrow(() -> new ReportNotFoundException(id));
    }

    public List<Report> findAll() {
        return reportGateway.findAll();
    }

    public Report findByIdAndUserId(UUID id, UUID userId) {
        return reportGateway.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ReportNotFoundException(id));
    }

    public List<Report> findAllByUserId(UUID userId) {
        return reportGateway.findAllByUserId(userId);
    }

    public Report update(UUID id, Report report, User authenticatedUser) {
        Report existingReport = findByIdAndUserId(id, authenticatedUser.getId());
        Client client = clientUseCase.findByIdAndUserId(report.getClient().getId(), authenticatedUser.getId());
        
        existingReport.setTitle(report.getTitle());
        existingReport.setUser(authenticatedUser);
        existingReport.setClient(client);
        existingReport.setAnalysis(report.getAnalysis());
        existingReport.setTranscript(report.getTranscript());
        
        return reportGateway.save(existingReport);
    }

    public void delete(UUID id, UUID userId) {
        Report existingReport = findByIdAndUserId(id, userId);
        reportGateway.deleteById(existingReport.getId());
    }
}
