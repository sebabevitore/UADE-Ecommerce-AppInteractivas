package com.uade.ecommerce.dto.response;

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


