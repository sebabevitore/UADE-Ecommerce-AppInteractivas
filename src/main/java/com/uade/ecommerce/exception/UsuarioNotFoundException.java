package com.uade.ecommerce.exception;

<<<<<<< Updated upstream
public class UsuarioNotFoundException {
    
=======
public class UsuarioNotFoundException extends RuntimeException {
    public UsuarioNotFoundException(String mensaje) {
        super(mensaje);
    }
>>>>>>> Stashed changes
}
