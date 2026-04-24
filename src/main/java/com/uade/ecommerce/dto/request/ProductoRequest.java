package com.uade.ecommerce.dto.request;

import java.util.List;

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
    private int stock; // Stock/cantidad disponible del producto
    private List<Long> categoriaIds;
}
