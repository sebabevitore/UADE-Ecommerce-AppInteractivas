package com.uade.ecommerce.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CarritoResponse {
    private List<ItemCarritoResponse> items;
    private double total;

    




}
