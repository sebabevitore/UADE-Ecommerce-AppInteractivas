package com.uade.ecommerce.exception;

public class CarritoNotFoundException extends RuntimeException {
    public CarritoNotFoundException(String mensaje) {
        super(mensaje);
    }
}
