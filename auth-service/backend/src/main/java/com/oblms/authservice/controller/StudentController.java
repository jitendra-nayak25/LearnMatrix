package com.oblms.authservice.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student")
public class StudentController {

    @GetMapping("/me")
    public Map<String, String> me(Authentication auth) {

        String authority =
                auth.getAuthorities().iterator().next().getAuthority();

        return Map.of(
                "email", auth.getName(),
                "role", authority.replace("ROLE_", "")
        );
    }

    @GetMapping("/courses")
    public List<Map<String, Object>> courses() {

        return List.of(
                Map.of(
                        "code", "CS101",
                        "title", "Data Structures",
                        "semester", 3
                ),
                Map.of(
                        "code", "CS205",
                        "title", "Database Systems",
                        "semester", 5
                )
        );
    }
}
