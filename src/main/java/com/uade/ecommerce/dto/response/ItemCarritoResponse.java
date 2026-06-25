package com.uade.ecommerce.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ItemCarritoResponse {
    private Long id;
    private Long id_producto;
    private int cantidad;
    private double precioUnitario;
    private String nombreProducto;
    private String imagen;
    
}
