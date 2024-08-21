package com.aws.s3_file_upload.services;

import com.aws.s3_file_upload.ExceptionHandling.ImageUploadException;
import org.springframework.web.multipart.MultipartFile;

import java.util.NoSuchElementException;

public interface FileUploader {
    String uploadFile(MultipartFile file) ;
}
