package com.uade.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class RegisterRequest { //DTO para representar la solicitud de registro de un nuevo usuario con email, contraseña, nombre y apellido
    private String email; // Email del nuevo usuario que se va a registrar
    private String password; // Contraseña del nuevo usuario que se va a registrar
    private String nombre; // Nombre del nuevo usuario que se va a registrar
    private String apellido; // Apellido del nuevo usuario que se va a registrar
}
