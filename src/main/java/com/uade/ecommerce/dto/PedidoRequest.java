package com.uade.ecommerce.dto;

import lombok.Data;
import java.util.List;

@Data
public class PedidoRequest { //DTO para crear un nuevo pedido
    private List<ItemPedidoRequest> items; // Lista de productos y cantidades
}

@Data
class ItemPedidoRequest { // DTO para cada item del pedido
    private Long productoId; // ID del producto
    private int cantidad; // Cantidad del producto a comprar
}
