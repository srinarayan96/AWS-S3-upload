package com.aws.s3_file_upload.services.impl;

import com.aws.s3_file_upload.ExceptionHandling.ImageUploadException;
import com.aws.s3_file_upload.models.FileInfo;
import com.aws.s3_file_upload.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
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

    @Override
    public List<FileInfo> listAllFiles() {
        ListObjectsV2Request listV2Request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();
        var response = s3Client.listObjectsV2(listV2Request);
        return response.contents().stream().map(s3Object -> new FileInfo(s3Object.key(), preSignedUrl(s3Object.key()))).toList();

    }

    @Override
    public FileInfo fetchFile(String fileName) {
        String url = preSignedUrl(fileName);
        return new FileInfo(fileName, url);
    }

    private String preSignedUrl(String fileName){
        if(fileName == null){
            throw new ImageUploadException("No filename");
        }
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
