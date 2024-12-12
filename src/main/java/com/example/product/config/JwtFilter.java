package com.example.product.config;

import com.example.product.service.JwtService;
import com.example.product.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    // private final MyUserDetailsService userDetailsService;
    private final UserDetailsService userDetailsService;


    public JwtFilter(JwtService jwtService, MyUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        //Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsImV4cCI6MTY4MzU3NTYwMH0.RM5b7h7wN4v3t9tbMDKZcQAYExO7IlJeZFrJBrXzib4
        final String authHeader = request.getHeader("Authorization");

        /*
        wenn der Header entweder fehlt oder nicht korrekt gesetzt ist,
        wird die Anfrage an den nächsten Filter in der Kette weitergegeben
         */
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response); // anfrage an der nächsten Filter weitergeben
            return;
        }

        final String jwt = authHeader.substring(7); // gibt JWT-Token zurück
        final String username = jwtService.extractUserName(jwt); // username wird aus dem Token extrahiert

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // gibt die aktuellen Anmeldedaten des Benutzers


        // startzustand ist am anfang immer null (addFilterBefore), wenn die Anfrage neu ist
        if (username != null && authentication == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // im Security Kontext speichern
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()

                );
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }


        }

        filterChain.doFilter(request, response);
    }
}
