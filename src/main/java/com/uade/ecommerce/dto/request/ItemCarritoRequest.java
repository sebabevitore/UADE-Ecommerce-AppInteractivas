package com.uade.ecommerce.dto.request;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ItemCarritoRequest {
    @JsonProperty("id_producto") // Esto le dice a Java: "Si recibes 'id_producto' del JSON, ponlo aquí"
    private Long idProducto;
    private int cantidad;
}
