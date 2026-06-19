package com.gume.sonar.infrastructure.provider;

import com.gume.sonar.application.gateway.FileGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.net.URI;
import java.time.Duration;

@Component
public class S3FileDataProvider implements FileGateway {

    @Value("${aws.s3.bucket.sonar-audio}")
    private String bucketName;

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    @Value("${aws.s3.endpoint:#{null}}")
    private String endpointOverride;

    // 1. Injetamos o profile ativo. Deixamos "prod" como fallback (padrão) caso não encontre
    @Value("${spring.profiles.active:prod}")
    private String activeProfile;

    @Override
    public String generateUploadUrl(String fileKey, String contentType) {
        
        S3Presigner.Builder presignerBuilder = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create());

        // 2. Agora verificamos explicitamente se o profile é "dev" antes de aplicar o override
        if ("dev".equalsIgnoreCase(activeProfile) && endpointOverride != null && !endpointOverride.isBlank()) {
            presignerBuilder.endpointOverride(URI.create(endpointOverride))
                    .serviceConfiguration(S3Configuration.builder()
                            .pathStyleAccessEnabled(true)
                            .build());
        }

        try (S3Presigner presigner = presignerBuilder.build()) {

            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .contentType(contentType)
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(15))
                    .putObjectRequest(objectRequest)
                    .build();

            PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);
            return presignedRequest.url().toString();
            
        } catch (Exception e) {
            // Log do erro real para ajudar no debug
            System.err.println("Erro ao gerar Pre-Signed URL do S3: " + e.getMessage());
            return "https://mock-s3-bucket.s3.amazonaws.com/" + fileKey + "?presigned=mock";
        }
    }

    @Override
    public String generateDownloadUrl(String fileKey) {
        try (S3Presigner presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build()) {

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .build();

            // Gera uma URL que se autodestrói em 2 horas
            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofHours(2)) 
                    .getObjectRequest(getObjectRequest)
                    .build();

            return presigner.presignGetObject(presignRequest).url().toString();
            
        } catch (Exception e) {
            System.err.println("Erro ao gerar Download URL: " + e.getMessage());
            throw new RuntimeException("Falha ao gerar link de download do áudio", e);
        }
    }
}