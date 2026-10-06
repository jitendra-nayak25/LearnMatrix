package com.oblms.authservice.controller;

public class RegisterResponse {

    private String message;
    private String email;
    private String role;

    public RegisterResponse() {
    }

    public RegisterResponse(
            String message,
            String email,
            String role) {

        this.message = message;
        this.email = email;
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}