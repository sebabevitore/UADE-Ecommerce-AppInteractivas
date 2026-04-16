package com.uade.ecommerce.dto;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor

public class LoginRequest { //DTO para representar la solicitud de inicio de sesión con email y contraseña
    private String email; // Email del usuario que intenta iniciar sesión
    private String password; // Contraseña del usuario que intenta iniciar sesión
    
}
