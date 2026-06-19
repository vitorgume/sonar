package com.gume.sonar.application.usecase;

import com.gume.sonar.application.gateway.FileGateway;
import com.gume.sonar.application.gateway.ReportGateway;
import com.gume.sonar.domain.Report;
import com.gume.sonar.domain.Transcricao;
import com.gume.sonar.domain.enums.ReportStatus;
import com.gume.sonar.domain.exception.ReportNotFoundException;
import com.gume.sonar.infrastructure.provider.dto.BuscaTranscricaoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportUseCase {

    private final ReportGateway reportGateway;
    private final TranscricaoApiUseCase transcricaoApiUseCase;
    private final TranscricaoUseCase transcricaoUseCase;
    private final AnaliseIAUseCase analiseIAUseCase;
    private final FileGateway fileGateway;

    public Report create(Report report) {
    
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
        Report report = findById(transcricao.getReport().getId());
        report.setTranscript(transcriptionDto.getText());

        // 4. Call AnaliseIA to analyze the text
        String analysis = analiseIAUseCase.analisarTranscricao(report.getTranscript());
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

    public Report update(UUID id, Report report) {
        Report existingReport = findById(id);
        
        existingReport.setTitle(report.getTitle());
        existingReport.setUser(report.getUser());
        existingReport.setClient(report.getClient());
        existingReport.setAnalysis(report.getAnalysis());
        existingReport.setTranscript(report.getTranscript());
        
        return reportGateway.save(existingReport);
    }

    public void delete(UUID id) {
        Report existingReport = findById(id);
        reportGateway.deleteById(existingReport.getId());
    }
}
