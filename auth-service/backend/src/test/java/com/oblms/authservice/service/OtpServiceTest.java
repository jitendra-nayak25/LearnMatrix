package com.oblms.authservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OtpServiceTest {

    @Mock
    private JavaMailSender mailSender;

    private OtpService otpService;

    @BeforeEach
    void setUp() {
        otpService = new OtpService(mailSender);
    }

    @Test
    void testSendAndCheckOtp_Success() {
        String email = "test@learnmatrix.com";
        otpService.sendOtp(email);

        // We test with whatever was saved in otpStore
        // checkOtp without knowing the exact code with wrong code first
        assertFalse(otpService.checkOtp(email, "000000_invalid"));

        // Now find the valid OTP using a test check or verify check
        boolean foundValid = false;
        for (int i = 0; i <= 999999; i++) {
            String candidate = String.format("%06d", i);
            if (otpService.checkOtp(email, candidate)) {
                foundValid = true;
                // checkOtp should NOT clear the OTP
                assertTrue(otpService.checkOtp(email, candidate));
                // verifyOtp should clear the OTP
                assertTrue(otpService.verifyOtp(email, candidate));
                // After verifyOtp, it should be cleared
                assertFalse(otpService.checkOtp(email, candidate));
                break;
            }
        }
        assertTrue(foundValid, "A 6-digit OTP should have been generated and verifiable");
    }

    @Test
    void testVerifyOtp_NonExistentEmail_ReturnsFalse() {
        assertFalse(otpService.verifyOtp("unknown@learnmatrix.com", "123456"));
        assertFalse(otpService.checkOtp("unknown@learnmatrix.com", "123456"));
    }
}
