package com.example.educacion.service;

import com.example.educacion.entity.TokenBlacklist;
import com.example.educacion.repository.TokenBlacklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class TokenBlacklistService {

    @Autowired
    private TokenBlacklistRepository tokenBlacklistRepository;

    @Transactional
    public void blacklistingToken(String token, Date expirationDate) {
        if (tokenBlacklistRepository.existsByToken(token)) {
            return;
        }
        TokenBlacklist tokenBlacklist = new TokenBlacklist();
        tokenBlacklist.setToken(token);
        
        // Convert Date to LocalDateTime, fallback to now + 24 hours if null
        LocalDateTime expiry;
        if (expirationDate != null) {
            expiry = expirationDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
        } else {
            expiry = LocalDateTime.now().plusDays(1);
        }
        
        tokenBlacklist.setFechaExpiracion(expiry);
        tokenBlacklistRepository.save(tokenBlacklist);
    }

    @Transactional(readOnly = true)
    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklistRepository.existsByToken(token);
    }
}
