package com.example.product.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    private final String secretKeyString = "91obJsfm/xtDJ2rFj75tv8bMKS6OfbqSLX6fvzgq+7A=";

    public String generateToken(Authentication authenticate) {
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Benutzername direkt abrufen
        String username = authenticate.getName();

        return Jwts.builder()
                .subject(username)            // Benutzername setzen
                .issuedAt(new Date())         // Erstellungsdatum
                .expiration(new Date(System.currentTimeMillis() + 60 * 10 * 1000))  // Ablaufdatum +10min
                .signWith(getSignKey())             // Token signieren
                .compact();
    }


    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKeyString);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
