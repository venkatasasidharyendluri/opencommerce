package com.opencommerce.authservice.controller;

import com.opencommerce.authservice.dto.request.*;
import com.opencommerce.authservice.dto.response.ApiResponse;
import com.opencommerce.authservice.dto.response.AuthResponse;
import com.opencommerce.authservice.dto.response.UserResponse;
import com.opencommerce.authservice.security.JwtService;
import com.opencommerce.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request){
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(
            @RequestHeader("Authorization")
            String authHeader
    ) {

        String jwt =
                authHeader.substring(7);

        return ResponseEntity.ok(
                authService.getCurrentUser(
                        jwtService.extractEmail(jwt)
                )
        );
    }


    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(
            @RequestHeader("Authorization")
            String authHeader
    ) {

        String jwt =
                authHeader.substring(7);

        return ResponseEntity.ok(
                authService.logout(
                        jwtService.extractUserUuid(
                                jwt
                        )
                )
        );
    }

    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse> verifyEmail(@RequestParam String token) {
        return ResponseEntity.ok(authService.verifyEmail(token));
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<ApiResponse>
    resendVerificationEmail(@Valid @RequestBody ResendVerificationRequest request) {
        return ResponseEntity.ok(authService.resendVerificationEmail(request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(authService.resetPassword(request));
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        return ResponseEntity.ok(authService.forgotPassword(request));
    }
//    @GetMapping("/admin")
//    @PreAuthorize("hasRole('ADMIN')")
//    public String admin() {
//        return "Admin";
//    }
//    @GetMapping("/customer")
//    @PreAuthorize("hasRole('CUSTOMER')")
//    public String customer() {
//        return "Customer";
//    }
}
