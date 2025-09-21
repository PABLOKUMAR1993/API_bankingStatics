package com.banking.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    
    private String token;
    private String email;
    private String nombreUsuario;
    private Boolean activo;
    private String message;
    
    public AuthResponse(String message) {
        this.message = message;
    }

}