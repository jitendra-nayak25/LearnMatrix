package com.oblms.authservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "JWT authentication is working!";
    }

    @GetMapping("/test/admin")
    public String adminTest() {
        return "ADMIN access successful!";
    }

    @GetMapping("/test/faculty")
    public String facultyTest() {
        return "FACULTY access successful!";
    }

    @GetMapping("/test/student")
    public String studentTest() {
        return "STUDENT access successful!";
    }
}