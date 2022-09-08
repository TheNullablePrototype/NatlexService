package com.prototype.natlexservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExportFileNotReadyException.class)
    public ResponseEntity<?> handleExportFileNotReady(ExportFileNotReadyException ex) {
        return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).build();
    }

    @ExceptionHandler(NotAuthorizationException.class)
    public ResponseEntity<?> handleAuthorizationForbidden(NotAuthorizationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
