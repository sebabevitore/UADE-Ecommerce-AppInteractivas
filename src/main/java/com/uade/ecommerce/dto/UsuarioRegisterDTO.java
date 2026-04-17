package com.uade.ecommerce.dto;
import java.time.LocalDate;

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
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private Sexo sexo;
    private LocalDate fechaNacimiento; 

}

