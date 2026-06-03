package com.techplanner.clientes_service.domain.exception;

public class ClienteServiceException extends RuntimeException {
    public ClienteServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClienteServiceException(String message) {
        super(message);
    }
}
