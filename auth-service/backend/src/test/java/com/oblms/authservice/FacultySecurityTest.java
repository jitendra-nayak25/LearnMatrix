package com.oblms.authservice;

import com.oblms.authservice.config.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.core.env.Environment;
import org.springframework.test.web.servlet.MockMvc;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FacultySecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private Environment environment;

    private String facultyToken;
    private String adminToken;

    @BeforeEach
    void setUp() {
        facultyToken = jwtService.generateToken("faculty@test.local", "FACULTY");
        adminToken = jwtService.generateToken("admin@test.local", "ADMIN");
    }

    @Test
    void facultyCanAccessFacultyCourses() throws Exception {
        mockMvc.perform(get("/faculty/courses")
                        .header("Authorization", "Bearer " + facultyToken))
                .andExpect(status().isOk());
    }

    @Test
    void facultySeesItsOwnIdentityFromToken() throws Exception {
        mockMvc.perform(get("/faculty/me")
                        .header("Authorization", "Bearer " + facultyToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("faculty@test.local"))
                .andExpect(jsonPath("$.role").value("FACULTY"));
    }

    @Test
    void adminIsRejectedOnFacultyRoutes() throws Exception {
        mockMvc.perform(get("/faculty/courses")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void facultyIsRejectedOnAdminUsers() throws Exception {
        mockMvc.perform(get("/auth/users")
                        .header("Authorization", "Bearer " + facultyToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminCanListUsers() throws Exception {
        mockMvc.perform(get("/auth/users")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    @Test
    void missingTokenIsRejected() throws Exception {
        mockMvc.perform(get("/faculty/courses"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void expiredTokenReturns401WithClearMessage() throws Exception {
        String expired = Jwts.builder()
                .subject("faculty@test.local")
                .claim("role", "FACULTY")
                .issuedAt(Date.from(Instant.now().minusSeconds(7200)))
                .expiration(Date.from(Instant.now().minusSeconds(3600)))
                .signWith(secretKey())
                .compact();

        mockMvc.perform(get("/faculty/courses")
                        .header("Authorization", "Bearer " + expired))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message")
                        .value("Token expired, please login again"));
    }

    @Test
    void tamperedTokenReturns401() throws Exception {
        String[] parts = facultyToken.split("\\.");
        String tampered = parts[0] + "." + parts[1] + ".AAAA";

        mockMvc.perform(get("/faculty/courses")
                        .header("Authorization", "Bearer " + tampered))
                .andExpect(status().isUnauthorized());
    }

    private SecretKey secretKey() {
        String secret = environment.getProperty("app.jwt.secret");
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
