//v2

package com.example.product.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    private final String secretKeyString = "91obJsfm/xtDJ2rFj75tv8bMKS6OfbqSLX6fvzgq+7A=";

    public String generateToken(Authentication authenticate) {
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Benutzername direkt abrufen
        String username = authenticate.getName();
       // System.out.println(username);

        return Jwts.builder()
                .subject(username)            // Benutzername setzen
                .issuedAt(new Date())         // Erstellungsdatum
                .expiration(new Date(System.currentTimeMillis() + 60 * 10 * 1000))  // Ablaufdatum +10min
                .signWith(getSignKey())             // Token signieren
                .compact();
    }


    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKeyString);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String jwtToken) {

        return extractClaims(jwtToken, Claims::getSubject);
    }


    public boolean isTokenValid(String jwtToken, UserDetails userDetails) {
        final String userName = extractUserName(jwtToken);
        return userName.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken);
    }

    private boolean isTokenExpired(String jwtToken) {
        return extractExpiration(jwtToken).before(new Date());
    }

    private Date extractExpiration(String jwtToken) {
        return extractClaims(jwtToken, Claims::getExpiration);
    }

    private <T> T extractClaims(String jwtToken, Function<Claims, T> claimResolver) {
        Claims claims = extractClaims(jwtToken);
        return claimResolver.apply(claims);
    }

    private Claims extractClaims(String jwtToken) {
        return Jwts
                .parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

}
