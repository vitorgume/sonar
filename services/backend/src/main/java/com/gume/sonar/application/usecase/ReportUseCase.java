package com.gume.sonar.application.usecase;

import com.gume.sonar.application.gateway.ReportGateway;
import com.gume.sonar.domain.Report;
import com.gume.sonar.domain.exception.ReportNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportUseCase {

    private final ReportGateway reportGateway;

    public Report create(Report report) {
        // To be implemented later
        if (report.getId() == null) {
            report.setId(UUID.randomUUID());
        }
        return report;
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
