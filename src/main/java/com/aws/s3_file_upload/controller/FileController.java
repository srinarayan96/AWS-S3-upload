package com.aws.s3_file_upload.controller;

import com.aws.s3_file_upload.services.FileUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {
    private final FileUploader fileUploader;

    @PostMapping(value = "/image/upload")
    public ResponseEntity<String> uploadImage(MultipartFile image) {
        return ResponseEntity.ok(fileUploader.uploadFile(image));
    }
}
