package com.uade.ecommerce.dto.request;

import lombok.Data;

@Data
public class ItemCarritoRequest {
    private Long id_producto;
    private int cantidad;
}
