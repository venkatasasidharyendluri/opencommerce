package com.opencommerce.authservice.service.impl;

import com.opencommerce.authservice.dto.request.*;
import com.opencommerce.authservice.dto.response.ApiResponse;
import com.opencommerce.authservice.dto.response.AuthResponse;
import com.opencommerce.authservice.dto.response.UserResponse;
import com.opencommerce.authservice.entity.*;
import com.opencommerce.authservice.enums.RoleType;
import com.opencommerce.authservice.exception.InvalidCredentialsException;
import com.opencommerce.authservice.exception.RefreshTokenNotFoundException;
import com.opencommerce.authservice.exception.RoleNotFoundException;
import com.opencommerce.authservice.exception.UserAlreadyExistsException;
import com.opencommerce.authservice.mapper.UserMapper;
import com.opencommerce.authservice.repository.*;
import com.opencommerce.authservice.security.JwtService;
import com.opencommerce.authservice.service.AuthService;
import com.opencommerce.authservice.service.EmailService;
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
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final EmailService emailService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;
    @Value("${jwt.token-type}")
    private String tokenType;
    @Value("${domain}")
    private String domain;

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

        String verificationToken = UUID.randomUUID().toString();

        EmailVerificationToken emailToken = EmailVerificationToken.builder().
                token(verificationToken)
                .expiresAt(LocalDateTime.now().plusHours(24))
                .user(user)
                .build();
        emailVerificationTokenRepository.save(emailToken);

        String verificationLink = domain + "/api/v1/auth/verify-email?token=" + verificationToken;

        emailService.sendVerificationEmail(user.getEmail(), verificationLink);

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
        if (!user.isEmailVerified()) {
            throw new InvalidCredentialsException(
                    "Please verify your email first"
            );
        }

        String accessToken =
                jwtService.generateToken(user);
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
                        () -> new RefreshTokenNotFoundException("Refresh token not found")
                );
        if( refreshToken .getExpiresAt() .isBefore( LocalDateTime .now() ) ){
            throw new InvalidCredentialsException("Refresh Token Expired");
        }
        User user = refreshToken.getUser();
        String accessToken =
                jwtService.generateToken(user);
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

    @Override
    @Transactional
    public ApiResponse verifyEmail( String token ) {

        EmailVerificationToken emailToken =
                emailVerificationTokenRepository
                        .findByToken(token)
                        .orElseThrow( () -> new InvalidCredentialsException( "Invalid verification token")
                        );

        if ( emailToken.getExpiresAt().isBefore(LocalDateTime.now()) )
        {
            emailVerificationTokenRepository.delete(emailToken);
            throw new InvalidCredentialsException("Verification token expired");
        }

        User user = emailToken.getUser();

        user.setEmailVerified(true);

        userRepository.save(user);

        emailVerificationTokenRepository.delete(emailToken);
        return new ApiResponse(true, "Email verified successfully");
    }

    @Override
    @Transactional
    public ApiResponse resendVerificationEmail(ResendVerificationRequest request) {

        User user =
                userRepository.findByEmail(request.email())
                        .orElseThrow(() -> new InvalidCredentialsException("User not found"));

        if (user.isEmailVerified())
        {
            return new ApiResponse(true, "Email already verified");
        }


        EmailVerificationToken emailToken =
                emailVerificationTokenRepository
                        .findByUser(user)
                        .orElse(
                                EmailVerificationToken
                                        .builder()
                                        .user(user)
                                        .build()
                        );

        String verificationToken =
                UUID.randomUUID().toString();

        emailToken.setToken(verificationToken);
        emailToken.setExpiresAt(
                LocalDateTime.now().plusHours(24)
        );

        emailVerificationTokenRepository.save(emailToken);

        String verificationLink ="http://"+ domain + "/api/v1/auth/verify-email?token=" + verificationToken;

        emailService.sendVerificationEmail(user.getEmail(), verificationLink);
        return new ApiResponse(true, "Verification email sent");
    }

    @Override
    @Transactional
    public ApiResponse forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow( () ->
                        new InvalidCredentialsException("Account with this email not found")
                );
        PasswordResetToken resetToken =
                passwordResetTokenRepository
                        .findByUser(user)
                        .orElse(PasswordResetToken.builder().user(user).build());

        String token = UUID.randomUUID().toString();

        resetToken.setToken(token);

        resetToken.setExpiresAt(LocalDateTime.now().plusHours(1)
        );

        passwordResetTokenRepository.save(resetToken);
        String resetLink = domain + "/api/v1/auth/reset-password?token=" + token;

        emailService.sendPasswordResetEmail(user.getEmail(), resetLink);

        return new ApiResponse(
                true,
                "Password reset email sent"
        );

    }

    @Override
    @Transactional
    public ApiResponse resetPassword(ResetPasswordRequest request) {

        PasswordResetToken resetToken =
                passwordResetTokenRepository
                        .findByToken(request.token())
                        .orElseThrow(
                                () -> new InvalidCredentialsException("Invalid reset token")
                        );

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            passwordResetTokenRepository.delete(resetToken);
            throw new InvalidCredentialsException("Reset token expired");
        }

        User user = resetToken.getUser();

        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));

        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        passwordResetTokenRepository.delete(resetToken);

        return new ApiResponse(true, "Password reset successful");
    }

}
