package com.uade.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ProductoRequest { //DTO para representar la solicitud de creación o actualización de un producto con su nombre, descripción, precio y categoría

    private String nombre; // Nombre del producto que se va a crear o actualizar
    private String descripcion; // Descripción del producto que se va a crear o actualizar
    private double precio; // Precio del producto que se va a crear o actualizar
    private Long categoriaId; // ID de la categoría a la que pertenece el producto que se va a crear o actualizar (se asume que las categorías ya existen en el sistema)

}
