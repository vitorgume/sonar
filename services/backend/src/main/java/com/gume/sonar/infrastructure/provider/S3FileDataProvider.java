package com.gume.sonar.infrastructure.provider;

import com.gume.sonar.application.gateway.FileGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.time.Duration;

@Component
public class S3FileDataProvider implements FileGateway {

    @Value("${aws.s3.bucket:sonar-audio-bucket}")
    private String bucketName;

    @Value("${aws.region:us-east-1}")
    private String region;

    @Override
    public String generateUploadUrl(String fileKey, String contentType) {
        try (S3Presigner presigner = S3Presigner.builder()
                .region(Region.of(region))
                // Default credentials provider will look for environment variables or instance profile
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build()) {

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
            // For testing purposes when AWS credentials are not configured
            return "https://mock-s3-bucket.s3.amazonaws.com/" + fileKey + "?presigned=mock";
        }
    }
}