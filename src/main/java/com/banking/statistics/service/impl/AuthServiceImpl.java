package com.banking.statistics.service.impl;

import com.banking.statistics.config.JwtUtil;
import com.banking.statistics.dto.AuthResponse;
import com.banking.statistics.dto.LoginRequest;
import com.banking.statistics.dto.RegisterRequest;
import com.banking.statistics.entity.User;
import com.banking.statistics.repository.UserRepository;
import com.banking.statistics.service.AuthService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse("Email already exists");
        }

        if (userRepository.existsByNombreUsuario(request.getNombreUsuario())) {
            return new AuthResponse("Username already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setNombreUsuario(request.getNombreUsuario());
        user.setContrasenya(passwordEncoder.encode(request.getContrasenya()));
        user.setActivo(false);

        userRepository.save(user);

        return new AuthResponse("User registered successfully. Waiting for admin activation.");
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {
            return new AuthResponse("Invalid credentials");
        }

        if (!passwordEncoder.matches(request.getContrasenya(), user.getContrasenya())) {
            return new AuthResponse("Invalid credentials");
        }

        if (!user.getActivo()) {
            return new AuthResponse("Account is not activated. Contact administrator.");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponse(
                token,
                user.getEmail(),
                user.getNombreUsuario(),
                user.getActivo(),
                "Login successful"
        );
    }

    @Override
    public AuthResponse logout() {
        return new AuthResponse("Logout successful");
    }

}