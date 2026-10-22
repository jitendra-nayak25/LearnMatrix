package com.oblms.authservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ErrorMessagesTest {

    @LocalServerPort
    private int port;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void wrongPasswordKeepsItsMessage() throws Exception {
        HttpResponse<String> res = postJson("/auth/login",
                Map.of("email", "Lucky@learnmatrix.com", "password", "WRONG"));

        assertEquals(401, res.statusCode());
        assertEquals("Wrong password", body(res).get("message"));
    }

    @Test
    void emailNotRegisteredKeepsItsMessage() throws Exception {
        HttpResponse<String> res = postJson("/auth/login",
                Map.of("email", "nobody@learnmatrix.com", "password", "x"));

        assertEquals(404, res.statusCode());
        assertEquals("Email not registered", body(res).get("message"));
    }

    @Test
    void invalidRefreshTokenKeepsItsMessage() throws Exception {
        HttpResponse<String> res = postJson("/auth/refresh",
                Map.of("refreshToken", "not-a-real-token"));

        assertEquals(401, res.statusCode());
        assertEquals("Invalid refresh token", body(res).get("message"));
    }

    @Test
    void loginResponseCarriesRefreshToken() throws Exception {
        HttpResponse<String> res = postJson("/auth/login",
                Map.of("email", "Lucky@learnmatrix.com", "password", "Lucky@123"));

        assertEquals(200, res.statusCode());
        Map<String, Object> body = body(res);
        assertNotNull(body.get("token"));
        assertNotNull(body.get("refreshToken"));
        assertEquals("ADMIN", body.get("role"));
    }

    private HttpResponse<String> postJson(String path, Map<String, String> payload)
            throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        objectMapper.writeValueAsString(payload)))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private Map<String, Object> body(HttpResponse<String> response)
            throws Exception {

        if (response.body() == null || response.body().isBlank()) {
            return Map.of();
        }

        JavaType type = TypeFactory.defaultInstance()
                .constructType(new TypeReference<Map<String, Object>>() {}.getType());

        return objectMapper.readValue(response.body(), type);
    }
}
