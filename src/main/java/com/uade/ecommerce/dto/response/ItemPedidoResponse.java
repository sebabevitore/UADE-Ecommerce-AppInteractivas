package com.uade.ecommerce.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemPedidoResponse { // DTO para representar cada item del pedido con su nombre, cantidad y precio unitario
    private String nombreProducto; // Nombre del producto
    private int cantidad; // Cantidad del producto en el pedido
    private double precioUnitario; // Precio unitario del producto en el momento del pedido (puede ser diferente al precio actual del producto)
}
