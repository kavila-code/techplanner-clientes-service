package com.techplanner.clientes_service.domain.exception;

public class ClienteNotFoundException extends RuntimeException {
    public ClienteNotFoundException(Long id) {
        super("Cliente not found: " + id);
    }
}
