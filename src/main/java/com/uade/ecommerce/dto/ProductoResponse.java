package com.uade.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductoResponse { //DTO para representar la respuesta de un producto con su ID, nombre, descripción, precio y categoría

    private Long id; // ID del producto
    private String nombre; // Nombre del producto
    private String descripcion; // Descripción del producto
    private double precio; // Precio del producto
    private String categoria; // Nombre de la categoría a la que pertenece el producto (se asume que las categorías ya existen en el sistema)

}
