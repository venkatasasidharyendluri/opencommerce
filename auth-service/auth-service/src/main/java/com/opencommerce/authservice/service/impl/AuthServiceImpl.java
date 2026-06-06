package com.opencommerce.authservice.service.impl;

import com.opencommerce.authservice.dto.request.LoginRequest;
import com.opencommerce.authservice.dto.request.RefreshTokenRequest;
import com.opencommerce.authservice.dto.request.RegisterRequest;
import com.opencommerce.authservice.dto.response.ApiResponse;
import com.opencommerce.authservice.dto.response.AuthResponse;
import com.opencommerce.authservice.dto.response.UserResponse;
import com.opencommerce.authservice.entity.RefreshToken;
import com.opencommerce.authservice.entity.Role;
import com.opencommerce.authservice.entity.User;
import com.opencommerce.authservice.enums.RoleType;
import com.opencommerce.authservice.exception.InvalidCredentialsException;
import com.opencommerce.authservice.exception.RefreshTokenNotFoundException;
import com.opencommerce.authservice.exception.RoleNotFoundException;
import com.opencommerce.authservice.exception.UserAlreadyExistsException;
import com.opencommerce.authservice.mapper.UserMapper;
import com.opencommerce.authservice.repository.RefreshTokenRepository;
import com.opencommerce.authservice.repository.RoleRepository;
import com.opencommerce.authservice.repository.UserRepository;
import com.opencommerce.authservice.security.JwtService;
import com.opencommerce.authservice.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${jwt.expiration}")
    private Long jwtExpiration;
    @Value("${jwt.token-type}")
    private String tokenType;

    @Override
    public ApiResponse register (RegisterRequest request){
        if(userRepository.existsByEmail(request.email())){
            throw new UserAlreadyExistsException(
                    "Email already exists"
            );
        }

        Role customerRole = roleRepository
                .findByName(RoleType.ROLE_CUSTOMER)
                .orElseThrow( () -> new RoleNotFoundException("customer role not found") );

        User user = User.builder()
                .uuid(UUID.randomUUID())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .emailVerified(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .roles(Set.of(customerRole))
                .build();
        userRepository.save(user);
        return new ApiResponse(true,"User Registered SuccessFully");
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
    try{
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),request.password()
                )
        );
    } catch ( BadCredentialsException ex){
        throw new InvalidCredentialsException("Invalid email or password");
    }


        User user = userRepository.findByEmail(request.email())
                                                .orElseThrow(() ->
                                                        new InvalidCredentialsException(
                                                                "Invalid Email or Password"
                                                        )
                );

        String accessToken =
                jwtService.generateToken(
                        user.getEmail()
                );
        String refreshToken =
                UUID.randomUUID().toString();

        RefreshToken token =
                refreshTokenRepository
                        .findByUser(user)
                        .orElse(
                                RefreshToken.builder()
                                        .user(user)
                                        .build()
                        );

        token.setToken(refreshToken);

        token.setExpiresAt(
                LocalDateTime.now()
                        .plusDays(7)
        );

        refreshTokenRepository.save(token);

        return new AuthResponse(
                accessToken,
                refreshToken,
                tokenType,
                jwtExpiration
        );
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(request.refreshToken())
                .orElseThrow(
                        () -> new RefreshTokenNotFoundException("")
                );
        if( refreshToken .getExpiresAt() .isBefore( LocalDateTime .now() ) ){
            throw new InvalidCredentialsException("Refresh Token Expired");
        }
        User user = refreshToken.getUser();
        String accessToken =
                jwtService.generateToken(
                        user.getEmail()
                );
        return new AuthResponse(
                accessToken,
                refreshToken.getToken(),
                tokenType,
                jwtExpiration
                );
    }

    @Override
    public UserResponse getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        String email =
                authentication.getName();
        User user = userRepository.findByEmail(email)
                        .orElseThrow( () -> new InvalidCredentialsException( "User not found" ) );

        return userMapper.toUserResponse(user);
    }

    @Override
    @Transactional
    public ApiResponse logout() {

        Authentication authentication = SecurityContextHolder.getContext()
                                                                                     .getAuthentication();

        String email = authentication.getName();

        User user = userRepository
                        .findByEmail(email)
                        .orElseThrow( () -> new InvalidCredentialsException( "User not found") );

        refreshTokenRepository.deleteByUser(user);

        return new ApiResponse(true, "Logout successful"
        );
    }
}
