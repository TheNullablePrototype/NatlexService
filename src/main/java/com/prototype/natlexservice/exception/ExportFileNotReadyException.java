package com.prototype.natlexservice.exception;

public class ExportFileNotReadyException extends RuntimeException {

    public ExportFileNotReadyException(String message) {
        super(message);
    }

    public ExportFileNotReadyException(String message, Throwable cause) {
        super(message, cause);
    }

}
