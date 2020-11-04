package org.task.exception;

public class FileSaveException extends RuntimeException {

    public FileSaveException(String message) {
        super(message);
    }

    public FileSaveException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileSaveException(Throwable cause) {
        super(cause);
    }
}
