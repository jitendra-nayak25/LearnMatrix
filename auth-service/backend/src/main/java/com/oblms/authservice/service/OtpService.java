package com.oblms.authservice.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

// Simple OTP service: 6-digit code, valid for 5 minutes, sent via Gmail.
@Service
public class OtpService {

    private final JavaMailSender mailSender;

    // email -> code
    private final Map<String, String> otpStore = new ConcurrentHashMap<>();
    // email -> expiry time
    private final Map<String, Long> expiryStore = new ConcurrentHashMap<>();

    private static final long VALIDITY = 5 * 60 * 1000; // 5 minutes

    public OtpService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStore.put(email, otp);
        expiryStore.put(email, System.currentTimeMillis() + VALIDITY);

        System.out.println("OTP for " + email + " : " + otp);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("LearnMatrix OTP Verification");
            message.setText("Your LearnMatrix OTP is: " + otp + "\nValid for 5 minutes.");
            mailSender.send(message);
        } catch (Exception e) {
            // Mail not configured or failed: OTP is still in console log above.
            System.out.println("Email send failed (check Gmail config): " + e.getMessage());
        }
    }

    public boolean verifyOtp(String email, String otp) {
        String saved = otpStore.get(email);
        Long expiry = expiryStore.get(email);
        if (saved == null || expiry == null) {
            return false;
        }
        if (System.currentTimeMillis() > expiry) {
            otpStore.remove(email);
            expiryStore.remove(email);
            return false;
        }
        boolean ok = saved.equals(otp);
        if (ok) {
            otpStore.remove(email);
            expiryStore.remove(email);
        }
        return ok;
    }
}
