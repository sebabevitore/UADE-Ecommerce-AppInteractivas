package com.uade.ecommerce.dto.request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String email; // Email del usuario que intenta iniciar sesión
    private String password; // Contraseña del usuario que intenta iniciar sesión
}
