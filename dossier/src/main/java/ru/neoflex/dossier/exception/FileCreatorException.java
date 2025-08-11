package ru.neoflex.dossier.exception;

public class FileCreatorException extends RuntimeException {
    public FileCreatorException(String message, Throwable cause) {
        super(message, cause);
    }
    public FileCreatorException(String message) {
        super(message);
    }
}