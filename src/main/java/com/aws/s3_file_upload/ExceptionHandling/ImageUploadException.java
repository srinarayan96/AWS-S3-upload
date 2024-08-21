package com.aws.s3_file_upload.ExceptionHandling;

import java.io.IOException;

public class ImageUploadException extends RuntimeException {
    public ImageUploadException(String msg) {
        super(msg);
    }
    public ImageUploadException(IOException e) {
        super(e);
    }
}
