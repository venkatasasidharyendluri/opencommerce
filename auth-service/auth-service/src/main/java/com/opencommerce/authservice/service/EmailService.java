package com.opencommerce.authservice.service;

public interface EmailService {

    void sendVerificationEmail(
            String to,
            String verificationLink
    );
}