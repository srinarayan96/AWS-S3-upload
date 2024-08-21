package com.aws.s3_file_upload.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class AppConfig {
    @Value("${aws.access-key}")
    private String accessKey;
    @Value("${aws.secret}")
    private String secretKey;
    @Bean
    public S3Client getS3Client(){
        return S3Client.builder()
                .credentialsProvider(() -> AwsBasicCredentials.create(accessKey, secretKey))
                .region(Region.AP_SOUTH_1)
                .build();
    }
    @Bean
    public S3Presigner getPresigner(){
        return S3Presigner.builder()
                .credentialsProvider(() -> AwsBasicCredentials.create(accessKey, secretKey))
                .region(Region.AP_SOUTH_1)
                .build();
    }
}
