package com.uade.ecommerce.exception;

public class CarritoVacioException extends RuntimeException {
    public CarritoVacioException() {
        super("El carrito está vacío, no se puede realizar el pedido.");
    }
}