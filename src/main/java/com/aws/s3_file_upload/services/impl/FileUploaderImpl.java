package com.aws.s3_file_upload.services.impl;

import com.aws.s3_file_upload.ExceptionHandling.ImageUploadException;
import com.aws.s3_file_upload.services.FileUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploaderImpl implements FileUploader {
    @Value("${aws.bucket-name}")
    private String bucketName;

    private final S3Client s3Client;
    private final S3Presigner presigner;
    @Override
    public String uploadFile(MultipartFile file)  {
        String actualFileName = Optional.ofNullable(file).map(MultipartFile::getOriginalFilename).orElseThrow();
        String fileName = UUID.randomUUID()+actualFileName.substring(actualFileName.lastIndexOf("."));
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .contentLength(file.getSize())
                .key(fileName)
                .bucket(bucketName)
                .build();
        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            return preSignedUrl(fileName);
        } catch (IOException e) {
                throw new ImageUploadException("Error while connecting to AWS service");
        }
    }

    private String preSignedUrl(String fileName){
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();
            GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                    .getObjectRequest(getObjectRequest)
                    .signatureDuration(Duration.ofHours(2))
                    .build();
            var response = presigner.presignGetObject(getObjectPresignRequest);
            return response.url().toExternalForm();
        }finally {
            presigner.close();
        }

    }
}
