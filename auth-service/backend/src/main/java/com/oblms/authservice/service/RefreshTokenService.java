package com.oblms.authservice.service;

import com.oblms.authservice.entity.RefreshToken;
import com.oblms.authservice.repository.RefreshTokenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private static final long VALIDITY_SECONDS = 7L * 24 * 60 * 60;

    private final RefreshTokenRepository refreshTokens;

    public RefreshTokenService(RefreshTokenRepository refreshTokens) {
        this.refreshTokens = refreshTokens;
    }

    @Transactional
    public String issue(String email) {

        refreshTokens.deleteByExpiryBefore(Instant.now());

        RefreshToken token = new RefreshToken(
                email,
                UUID.randomUUID().toString(),
                Instant.now().plusSeconds(VALIDITY_SECONDS)
        );

        return refreshTokens.save(token).getToken();
    }

    @Transactional
    public RefreshToken rotate(String token) {

        RefreshToken existing = refreshTokens.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Invalid refresh token"));

        refreshTokens.delete(existing);

        if (Instant.now().isAfter(existing.getExpiry())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Refresh token expired, please login again");
        }

        return refreshTokens.save(new RefreshToken(
                existing.getEmail(),
                UUID.randomUUID().toString(),
                Instant.now().plusSeconds(VALIDITY_SECONDS)
        ));
    }

    @Transactional
    public void revoke(String email) {
        refreshTokens.deleteByEmail(email);
    }
}
