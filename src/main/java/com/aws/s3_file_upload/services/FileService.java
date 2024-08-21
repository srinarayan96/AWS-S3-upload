package com.aws.s3_file_upload.services;

import com.aws.s3_file_upload.models.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    String uploadFile(MultipartFile file) ;
    List<FileInfo> listAllFiles();

    FileInfo fetchFile(String fileName);
}
