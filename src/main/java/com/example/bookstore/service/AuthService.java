package com.example.bookstore.service;

import com.example.bookstore.model.dto.AuthenticationRequest;
import com.example.bookstore.model.dto.TokenResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {
    TokenResponse authenticate(AuthenticationRequest request);
    UserDetails validateToken(String token);
}
