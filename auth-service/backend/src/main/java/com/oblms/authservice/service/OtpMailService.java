package com.oblms.authservice.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

// Sends the OTP mail in a background thread so the
// HTTP request returns instantly, even if Gmail is slow.
@Service
public class OtpMailService {

    private final JavaMailSender mailSender;

    public OtpMailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendOtpMail(String email, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("LearnMatrix OTP Verification");
            message.setText("Your LearnMatrix OTP is: " + otp + "\nValid for 5 minutes.");
            mailSender.send(message);
        } catch (Exception e) {
            // OTP is already stored and printed in console, so the
            // user flow still works even if Gmail is slow or blocked.
            System.out.println("Email send failed (check Gmail config): " + e.getMessage());
        }
    }
}
