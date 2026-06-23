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
    private final ClientUseCase clientUseCase;
    private final TranscricaoApiUseCase transcricaoApiUseCase;
    private final TranscricaoUseCase transcricaoUseCase;
    private final AnaliseIAUseCase analiseIAUseCase;
    private final FileGateway fileGateway;

    public Report create(UUID userId, Report report) {

        report.setUser(User.builder().id(userId).build());
        if (report.getCreationDate() == null) {
            report.setCreationDate(LocalDateTime.now());
        }

        if (report.getClient() != null && report.getClient().getId() != null) {
            Client client = clientUseCase.findById(userId, report.getClient().getId());
            report.setClient(client);
        }
    
        String urlSeguraParaDownload = fileGateway.generateDownloadUrl(report.getAudioFileKey());

        UUID transcriptionId = transcricaoApiUseCase.enviarTranscricao(urlSeguraParaDownload);
        
        // 2. Save Transcricao domain
        Transcricao transcricao = Transcricao.builder()
                .id(transcriptionId)
                .urlAudio(report.getAudioFileKey())
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
        Report report = findByIdInternal(transcricao.getReport().getId());
        report.setTranscript(transcriptionDto.getText());

        // 4. Call AnaliseIA to analyze the text
        String analysis = analiseIAUseCase.analisarTranscricao(report.getTranscript());
        report.setAnalysis(analysis);

        report.setStatus(ReportStatus.COMPLETED);
        
        reportGateway.save(report);
    }

    public Report findByIdInternal(UUID id) {
        return reportGateway.findById(id)
                .orElseThrow(() -> new ReportNotFoundException(id));
    }

    public Report findById(UUID userId, UUID id) {
        return reportGateway.findById(userId, id)
                .orElseThrow(() -> new ReportNotFoundException(id));
    }

    public List<Report> findAll(UUID userId) {
        return reportGateway.findAll(userId);
    }

    public Report update(UUID userId, UUID id, Report report) {
        Report existingReport = findById(userId, id);
        
        existingReport.setTitle(report.getTitle());
        if (report.getClient() != null && report.getClient().getId() != null) {
            Client client = clientUseCase.findById(userId, report.getClient().getId());
            existingReport.setClient(client);
        }
        existingReport.setAnalysis(report.getAnalysis());
        existingReport.setTranscript(report.getTranscript());
        
        return reportGateway.save(existingReport);
    }

    public void delete(UUID userId, UUID id) {
        Report existingReport = findById(userId, id);
        reportGateway.deleteById(userId, existingReport.getId());
    }
}
