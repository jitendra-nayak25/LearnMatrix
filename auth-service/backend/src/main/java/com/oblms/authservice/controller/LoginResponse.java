package com.oblms.authservice.controller;

public class LoginResponse {

    private String token;
    private String refreshToken;
    private String email;
    private String role;

    public LoginResponse() {
    }

    public LoginResponse(
            String token,
            String refreshToken,
            String email,
            String role) {

        this.token = token;
        this.refreshToken = refreshToken;
        this.email = email;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
