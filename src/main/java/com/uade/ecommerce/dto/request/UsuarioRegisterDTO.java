package com.uade.ecommerce.dto.request;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uade.ecommerce.model.Sexo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UsuarioRegisterDTO {
    private String nombre; // Nombre del usuario que se va a registrar
    private String apellido; // Apellido del usuario que se va a registrar
    private String email; // Email del usuario que se va a registrar 
    private String password; // Contraseña del usuario que se va a registrar 
    private Sexo sexo; // Sexo del usuario que se va a registrar 
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;  // Fecha de nacimiento del usuario que se va a registrar
}

