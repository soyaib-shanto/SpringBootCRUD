package com.example.loginfinal.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        Cookie[] cookies = request.getCookies();
        // String authorizationHeader = "";

         String jwtToken = "";

    // Check if cookies are present
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            // Look for the cookie named "jwt"
            if ("jwt".equals(cookie.getName())) {
                 jwtToken = "Bearer "+cookie.getValue();
                break;
            }
        }
    } else {
        System.out.println("No cookies found in the request");
    }



        String username = null;
        String jwt = null;

        // Check if the Authorization header is valid and starts with Bearer
        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
           
            jwt = jwtToken.substring(7);

            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                // Logging could be added here for JWT extraction errors
                System.out.println("JWT extraction error: " + e.getMessage());
            }
        }

        // Proceed if username is valid and no authentication exists in the security context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Validate JWT token with username
            if (jwtUtil.validateToken(jwt, userDetails.getUsername()) && !jwtUtil.isTokenExpired(jwt) ) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
