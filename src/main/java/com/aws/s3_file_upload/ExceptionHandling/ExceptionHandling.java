package com.aws.s3_file_upload.ExceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionHandling {
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> imageUploadException(ImageUploadException exc) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder().message(exc.getMessage())
                        .success(false)
                        .build());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> noSuchElementException(NoSuchElementException exc) {
        System.out.println("NoSuchElementException ----");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder()
                .message("Improper File upload")
                .success(false)
                .build());
    }

}
