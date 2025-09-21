package com.banking.statistics.service;

import com.banking.statistics.dto.AuthResponse;
import com.banking.statistics.dto.LoginRequest;
import com.banking.statistics.dto.RegisterRequest;

public interface AuthService {
    
    AuthResponse register(RegisterRequest request);
    
    AuthResponse login(LoginRequest request);
    
    AuthResponse logout();

}