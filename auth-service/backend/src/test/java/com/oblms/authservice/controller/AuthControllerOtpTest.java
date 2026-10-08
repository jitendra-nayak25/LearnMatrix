package com.oblms.authservice.controller;

import com.oblms.authservice.config.JwtService;
import com.oblms.authservice.entity.User;
import com.oblms.authservice.repository.UserRepository;
import com.oblms.authservice.service.OtpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerOtpTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private OtpService otpService;

    private AuthController authController;

    @BeforeEach
    void setUp() {
        authController = new AuthController(userRepository, passwordEncoder, jwtService, otpService);
    }

    @Test
    void testSendOtp() {
        OtpRequest req = new OtpRequest();
        req.setEmail("student@learnmatrix.com");

        String res = authController.sendOtp(req);
        assertEquals("OTP sent to email", res);
        verify(otpService).sendOtp("student@learnmatrix.com");
    }

    @Test
    void testVerifyOtp_Success() {
        when(otpService.checkOtp("student@learnmatrix.com", "123456")).thenReturn(true);

        OtpLoginRequest req = new OtpLoginRequest();
        req.setEmail("student@learnmatrix.com");
        req.setOtp("123456");

        String res = authController.verifyOtp(req);
        assertEquals("OTP verified", res);
    }

    @Test
    void testVerifyOtp_InvalidThrowsBadRequest() {
        when(otpService.checkOtp("student@learnmatrix.com", "999999")).thenReturn(false);

        OtpLoginRequest req = new OtpLoginRequest();
        req.setEmail("student@learnmatrix.com");
        req.setOtp("999999");

        assertThrows(ResponseStatusException.class, () -> authController.verifyOtp(req));
    }

    @Test
    void testLoginWithOtp_Success() {
        User user = new User("John", "student@learnmatrix.com", "hashed", "STUDENT");
        when(userRepository.findByEmail("student@learnmatrix.com")).thenReturn(Optional.of(user));
        when(otpService.verifyOtp("student@learnmatrix.com", "123456")).thenReturn(true);
        when(jwtService.generateToken("student@learnmatrix.com", "STUDENT")).thenReturn("jwt-token-xyz");

        OtpLoginRequest req = new OtpLoginRequest();
        req.setEmail("student@learnmatrix.com");
        req.setOtp("123456");

        LoginResponse res = authController.loginWithOtp(req);
        assertNotNull(res);
        assertEquals("jwt-token-xyz", res.getToken());
        assertEquals("student@learnmatrix.com", res.getEmail());
        assertEquals("STUDENT", res.getRole());
    }

    @Test
    void testRegisterWithOtp_Success() {
        when(userRepository.findByEmail("student@learnmatrix.com")).thenReturn(Optional.empty());
        when(otpService.checkOtp("student@learnmatrix.com", "123456")).thenReturn(true);
        when(passwordEncoder.encode("Pass123")).thenReturn("encoded-pass");

        User savedUser = new User("Student Name", "student@learnmatrix.com", "encoded-pass", "STUDENT");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        RegisterRequest req = new RegisterRequest();
        req.setName("Student Name");
        req.setEmail("student@learnmatrix.com");
        req.setPassword("Pass123");
        req.setRole("STUDENT");
        req.setOtp("123456");

        RegisterResponse res = authController.register(req);
        assertNotNull(res);
        assertEquals("User registered successfully", res.getMessage());
        assertEquals("student@learnmatrix.com", res.getEmail());
        verify(otpService).clearOtp("student@learnmatrix.com");
    }
}
