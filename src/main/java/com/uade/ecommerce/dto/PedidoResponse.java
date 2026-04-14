package com.uade.ecommerce.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.time.LocalDateTime;

@Data
@Builder
public class PedidoResponse { //DTO para representar un pedido con sus detalles
    private Long id; // ID del pedido
    private LocalDateTime fecha; // Fecha del pedido
    private double total; // Total del pedido
    private String estado; // Estado del pedido (e.g., "PENDIENTE", "ENVIADO", "ENTREGADO")
    private List<ItemPedidoResponse> detalles; // Lista de detalles del pedido (nombre del producto, cantidad, precio unitario)
}

@Data
@Builder
class ItemPedidoResponse { // DTO para representar cada item del pedido con su nombre, cantidad y precio unitario
    private String nombreProducto; // Nombre del producto
    private int cantidad; // Cantidad del producto en el pedido
    private double precioUnitario; // Precio unitario del producto en el momento del pedido (puede ser diferente al precio actual del producto)
}
