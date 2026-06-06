package com.opencommerce.authservice.service.impl;

import com.opencommerce.authservice.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    // Injected from application.properties: spring.mail.username
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendVerificationEmail(String to, String verificationLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            // 'true' enables multipart mode (needed for HTML + plain text fallback)
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, "OpenCommerce Team");
            helper.setTo(to);
            helper.setSubject("Verify Your Email Address - OpenCommerce");

            // HTML Content with inline CSS for better client compatibility
            String htmlContent = buildHtmlEmail(verificationLink);
            helper.setText(htmlContent, true); // 'true' indicates HTML

            // Optional: Add a plain-text fallback for clients that don't support HTML
            // helper.setText("Welcome! Please verify your email by clicking: " + verificationLink, htmlContent);

            mailSender.send(message);
            log.info("Verification email sent successfully to: {}", to);

        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Failed to send verification email to {}", to, e);
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    private String buildHtmlEmail(String verificationLink) {
        return """
        <!DOCTYPE html>
        <html>
        <head>
            <style>
                body { font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }
                .container { max-width: 600px; margin: 40px auto; background: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 12px rgba(0,0,0,0.1); }
                .header { background-color: #2c3e50; padding: 30px; text-align: center; }
                .header h1 { color: #ffffff; margin: 0; font-size: 24px; }
                .content { padding: 40px 30px; color: #333333; line-height: 1.6; }
                .button { display: inline-block; padding: 14px 28px; background-color: #3498db; color: #ffffff; text-decoration: none; border-radius: 5px; font-weight: bold; margin-top: 20px; }
                .button:hover { background-color: #2980b9; }
                .footer { background-color: #f9f9f9; padding: 20px; text-align: center; font-size: 12px; color: #888888; border-top: 1px solid #eeeeee; }
                .link-fallback { word-break: break-all; color: #3498db; }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="header">
                    <h1>Welcome to OpenCommerce</h1>
                </div>
                <div class="content">
                    <p>Hello,</p>
                    <p>Thank you for registering with <strong>OpenCommerce</strong>. To complete your account setup and start shopping, please verify your email address.</p>
                    <p style="text-align: center;">
                        <a href="%s" class="button">Verify Email Address</a>
                    </p>
                    <p>If the button above doesn't work, copy and paste the following link into your browser:</p>
                    <p class="link-fallback">%s</p>
                    <p>This link will expire in 24 hours.</p>
                    <p>Best regards,<br>The OpenCommerce Team</p>
                </div>
                <div class="footer">
                    <p>&copy; 2026 OpenCommerce. All rights reserved.</p>
                    <p>If you did not create an account, please ignore this email.</p>
                </div>
            </div>
        </body>
        </html>
        """.formatted(verificationLink, verificationLink);
    }
}