package com.uade.ecommerce.dto.request;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Data
@Builder
public class PedidoRequest {
    private Long id_usuario;
    private List<ItemPedidoRequest> items;
}
