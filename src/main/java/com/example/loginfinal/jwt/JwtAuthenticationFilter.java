package com.example.loginfinal.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getServletPath();
    return path.matches(".*(\\.css|\\.js|\\.png|\\.jpg|\\.jpeg|\\.ico)$")
        || path.startsWith("/css/")
        || path.equals("/favicon.ico")
        || path.equals("/login.html")
        || path.equals("/register.html")
        || path.equals("/register")
        || path.equals("/login1");
}

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


         Cookie[] cookies = request.getCookies();

         String jwtToken = "";

    // Check if cookies are present
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                    break;
                }
            }
        } else {
            System.out.println("No cookies found in the request");
        }


        String username = null;

        // Check if the Authorization header is valid and starts with Bearer
        if (jwtToken != null) {

            try {
                username = jwtUtil.extractUsername(jwtToken);
            } catch (Exception e) {
                System.out.println("JWT extraction error: " + e.getMessage());
            }
        }
        else{
            System.out.println("JWT Token is null");
            filterChain.doFilter(request, response);
            return;
        }

        // Proceed if username is valid and no authentication exists in the security context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Validate JWT token with username
            if (jwtUtil.validateToken(jwtToken, username) && !jwtUtil.isTokenExpired(jwtToken) ) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(username, null, null);

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                System.out.println("check-1");
            }
            else 
            {
                System.out.println("Token invalid or expaired check-2");
            }
        }
        else{
               System.out.println("Token invalid or expaired check-3");
        }

        filterChain.doFilter(request, response);
    }
}
