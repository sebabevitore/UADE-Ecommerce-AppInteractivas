package com.uade.ecommerce.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class PedidoRequest { //DTO para crear un nuevo pedido
    private List<ItemPedidoRequest> items; // Lista de productos y cantidades
}


