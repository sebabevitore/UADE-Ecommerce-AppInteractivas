package com.uade.ecommerce.exception;

public class ItemCarritoNotFoundException extends RuntimeException {
    public ItemCarritoNotFoundException(Long id) {
        super("No se encontró el item con id: " + id);
    }
}