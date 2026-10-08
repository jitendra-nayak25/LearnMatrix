package com.oblms.authservice.controller;

import com.oblms.authservice.config.JwtService;
import com.oblms.authservice.entity.User;
import com.oblms.authservice.repository.UserRepository;
import com.oblms.authservice.service.OtpService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final OtpService otpService;

    public AuthController(
            UserRepository userRepository,
            org.springframework.security.crypto.password.PasswordEncoder passwordEncoder,
            JwtService jwtService,
            OtpService otpService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.otpService = otpService;
    }

    // Step 1 of registration OR login-via-OTP: send OTP to email.
    @PostMapping("/otp/send")
    public String sendOtp(@RequestBody OtpRequest request) {
        otpService.sendOtp(request.getEmail());
        return "OTP sent to email";
    }

    // Step 2 of registration: verify OTP + create Student/Faculty only.
    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest request) {
        String role = request.getRole() == null ? "" : request.getRole().toUpperCase();

        // Admin cannot register, only Student or Faculty.
        if (!role.equals("STUDENT") && !role.equals("FACULTY")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only Student or Faculty can register");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
        }
        if (!otpService.verifyOtp(request.getEmail(), request.getOtp())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired OTP");
        }

        User user = new User(
                request.getName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                role);
        User saved = userRepository.save(user);
        return new RegisterResponse("User registered successfully", saved.getEmail(), saved.getRole());
    }

    // Login with email + password (password chosen at registration).
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
        String token = jwtService.generateToken(user.getEmail(), user.getRole());
        return new LoginResponse(token, user.getEmail(), user.getRole());
    }

    // Login with email + OTP (alternative to password).
    @PostMapping("/login-otp")
    public LoginResponse loginWithOtp(@RequestBody OtpLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email not registered"));
        if (!otpService.verifyOtp(request.getEmail(), request.getOtp())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired OTP");
        }
        String token = jwtService.generateToken(user.getEmail(), user.getRole());
        return new LoginResponse(token, user.getEmail(), user.getRole());
    }

    // Admin: list all Students and Faculty (no passwords sent).
    @GetMapping("/users")
    public List<User> listUsers(Authentication auth) {
        return userRepository.findAll().stream()
                .peek(u -> u.setPassword(null))
                .toList();
    }

    // Admin: delete a Student/Faculty by id.
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if ("ADMIN".equals(user.getRole())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete admin");
        }
        userRepository.delete(user);
        return "User deleted";
    }
}
