package com.oblms.authservice.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

// Simple OTP service: 6-digit code, valid for 5 minutes, sent via Gmail.
@Service
public class OtpService {

    private final OtpMailService otpMailService;

    // email -> code
    private final Map<String, String> otpStore = new ConcurrentHashMap<>();
    // email -> expiry time
    private final Map<String, Long> expiryStore = new ConcurrentHashMap<>();
    // email -> last send time (resend cooldown)
    private final Map<String, Long> lastSentStore = new ConcurrentHashMap<>();

    private static final long VALIDITY = 5 * 60 * 1000; // 5 minutes
    private static final long RESEND_INTERVAL = 60 * 1000; // 1 minute

    public OtpService(OtpMailService otpMailService) {
        this.otpMailService = otpMailService;
    }

    public void sendOtp(String email) {
        long now = System.currentTimeMillis();

        // Block rapid re-clicks: a fresh OTP was already sent, reuse it.
        Long last = lastSentStore.get(email);
        String existing = otpStore.get(email);
        Long expiry = expiryStore.get(email);
        if (last != null && now - last < RESEND_INTERVAL
                && existing != null && expiry != null && now < expiry) {
            throw new ResponseStatusException(
                    HttpStatus.TOO_MANY_REQUESTS,
                    "OTP already sent. Please wait a minute before resending.");
        }

        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStore.put(email, otp);
        expiryStore.put(email, now + VALIDITY);
        lastSentStore.put(email, now);

        System.out.println("OTP for " + email + " : " + otp);

        // Sent in background: HTTP returns instantly, OTP already stored.
        otpMailService.sendOtpMail(email, otp);
    }

    public boolean verifyOtp(String email, String otp) {
        boolean ok = checkOtp(email, otp);
        if (ok) {
            clearOtp(email);
        }
        return ok;
    }

    // Check without consuming (for 2-step registration: verify first, save later).
    public boolean checkOtp(String email, String otp) {
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
        return saved.equals(otp);
    }

    public void clearOtp(String email) {
        otpStore.remove(email);
        expiryStore.remove(email);
    }
}
