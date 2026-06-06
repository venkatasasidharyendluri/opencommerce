package com.opencommerce.authservice.service;

import com.opencommerce.authservice.dto.request.*;
import com.opencommerce.authservice.dto.response.ApiResponse;
import com.opencommerce.authservice.dto.response.AuthResponse;
import com.opencommerce.authservice.dto.response.UserResponse;

public interface AuthService {
    ApiResponse register(RegisterRequest request);

    AuthResponse login (LoginRequest request);

    AuthResponse refreshToken(RefreshTokenRequest request);

    UserResponse getCurrentUser();

    ApiResponse logout();

    ApiResponse verifyEmail(String token);

    ApiResponse resendVerificationEmail( ResendVerificationRequest request );
}
