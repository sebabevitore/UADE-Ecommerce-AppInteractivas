package com.uade.ecommerce.dto;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor

public class LoginRequest {
    private String email;
    private String password;
    
}
