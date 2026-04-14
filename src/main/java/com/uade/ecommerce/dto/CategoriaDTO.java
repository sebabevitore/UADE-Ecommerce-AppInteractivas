package com.uade.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaDTO { //DTO para representar una categoría con su ID y nombre

    private Long id; // ID de la categoría
    private String nombre; // Nombre de la categoría

}