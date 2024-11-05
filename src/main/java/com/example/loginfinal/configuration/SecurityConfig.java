package com.example.loginfinal.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.loginfinal.Services.CustomUserDetailsService;
import com.example.loginfinal.jwt.JwtAuthenticationFilter;
import com.example.loginfinal.jwt.JwtUtil;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

             @Autowired 
              CustomUserDetailsService userDetailsService;

            @Autowired
            JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


         return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth->auth
                .requestMatchers("/login.html", "/register.html", "/register","/login1").permitAll().anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, userDetailsService), UsernamePasswordAuthenticationFilter.class)
                    
                             .logout(logout -> logout
                             .logoutUrl("/logout")
                             .logoutSuccessUrl("/login.html")
                             .deleteCookies("jwt")   // Delete the JWT cookie
                             .invalidateHttpSession(true)  // Invalidate session on logout
                             .clearAuthentication(true)
                             .permitAll())
                             .build();
                        

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
   //SET1:
        // @Bean
        // CustomUserDetailsService customUserDetailsService() {
        //     return new CustomUserDetailsService();
        // }

         @Bean
         public PasswordEncoder passwordEncoder() {
             return new BCryptPasswordEncoder();
         }





   //SET2:
        // @Bean
        // public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,PasswordEncoder passwordEncoder) {
        //     DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        //     authenticationProvider.setUserDetailsService(userDetailsService);
        //     authenticationProvider.setPasswordEncoder(passwordEncoder);
        //     return new ProviderManager(authenticationProvider);
        // }
        // @Bean
        // public PasswordEncoder passwordEncoder() {
        //      return NoOpPasswordEncoder.getInstance(); // No password encoding
        //  }
         // @Bean
         // public PasswordEncoder passwordEncoder() {
         //     return new BCryptPasswordEncoder();
         // }

      
}
