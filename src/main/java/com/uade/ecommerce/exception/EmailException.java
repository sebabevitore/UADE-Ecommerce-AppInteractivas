package com.uade.ecommerce.exception;

public class EmailException extends RuntimeException {
    public EmailException() {
        super("El email ya está registrado en el sistema.");
    }    
}
