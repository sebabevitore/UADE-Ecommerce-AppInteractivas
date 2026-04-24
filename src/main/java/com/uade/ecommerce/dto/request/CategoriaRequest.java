package com.uade.ecommerce.dto.request;

import lombok.Data;

@Data
public class CategoriaRequest {
    private String nombre; // No lleva ID porque el usuario no lo elige
}
