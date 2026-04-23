package com.uade.ecommerce.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemPedidoRequest { // DTO para cada item del pedido
    private Long productoId; // ID del producto
    private int cantidad; // Cantidad del producto a comprar
}