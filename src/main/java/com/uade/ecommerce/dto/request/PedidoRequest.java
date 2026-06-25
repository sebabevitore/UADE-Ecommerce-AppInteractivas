package com.uade.ecommerce.dto.request;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class PedidoRequest {
    private Long id_usuario;
    private List<ItemPedidoRequest> items;
}
