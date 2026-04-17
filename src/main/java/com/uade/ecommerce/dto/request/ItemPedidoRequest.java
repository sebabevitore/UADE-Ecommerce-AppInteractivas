package com.uade.ecommerce.dto.request;

import lombok.Data;

@Data
public class ItemPedidoRequest { // DTO para cada item del pedido
    private Long productoId; // ID del producto
    private int cantidad; // Cantidad del producto a comprar
}