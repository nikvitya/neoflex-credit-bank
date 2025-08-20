package ru.neoflex.gateway.exceptions;

public class StatementStatusException extends RuntimeException {
    public StatementStatusException(String message) {
        super(message);
    }
}