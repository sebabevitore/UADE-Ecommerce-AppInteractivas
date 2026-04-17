package com.uade.ecommerce.dto.response;

import java.util.List;

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
    private List<String> categorias; // Lista de nombres de categorías a las que pertenece el producto (se asume que un producto puede pertenecer a varias categorías)

}
