package com.uade.ecommerce.exception;

public class InsufficientPermissionsException extends RuntimeException {
    public InsufficientPermissionsException(String mensaje) {
        super(mensaje);
    }
}