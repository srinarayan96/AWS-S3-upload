package com.aws.s3_file_upload.controller;

import com.aws.s3_file_upload.models.FileInfo;
import com.aws.s3_file_upload.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping(value = "/image/upload")
    public ResponseEntity<String> uploadImage(MultipartFile image) {
        return ResponseEntity.ok(fileService.uploadFile(image));
    }

    @GetMapping("/images")
    public ResponseEntity<List<FileInfo>> getFiles(){
        return ResponseEntity.ok(fileService.listAllFiles());
    }
    @GetMapping("/image/{file}")
    public ResponseEntity<FileInfo> getFile(@PathVariable("file") String fileName){
        return ResponseEntity.ok(fileService.fetchFile(fileName));
    }
}
