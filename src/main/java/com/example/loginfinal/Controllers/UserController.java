package com.example.loginfinal.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody; 

import com.example.loginfinal.Model.User;
import com.example.loginfinal.Services.CustomUserDetailsService;
import com.example.loginfinal.Services.UserServices;
import com.example.loginfinal.jwt.JwtUtil;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;


@Controller
public class UserController {

            @Autowired 
            CustomUserDetailsService customUserDetailsService;

            @Autowired 
            AuthenticationManager authenticationManager;

            @Autowired
            UserServices userServices;

            @Autowired
            JwtUtil jwtUtil;

            @Autowired
            private PasswordEncoder passwordEncoder;


            @PostMapping("/login1")
            public String login(@RequestParam String username, 
            @RequestParam String password, HttpServletResponse response) {
                try {

                    Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(username, password)
                    );
            
                    // Check if authentication was successful
                    if (authentication.isAuthenticated()) {

                        String jwt = jwtUtil.generateToken(username);

                         Cookie jwtCookie = new Cookie("jwt",jwt);
                         jwtCookie.setHttpOnly(true);
                         jwtCookie.setPath("/");
                         jwtCookie.setMaxAge(3600);
                         response.addCookie(jwtCookie);
                        // response.setHeader("Authorization", "Bearer " + jwt);
                        return "dashboard";
                    } else {
                        return "failed: Authentication was unsuccessful.";
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // Log the full stack trace
                    return "failed: " + e.getMessage();
                }
            }
            

        // @GetMapping("/dd")
        // public String dashboard() {
        //     return "dashboard";
        // }

       
        @GetMapping("/gg")
        @ResponseBody
        public List<User> getUsers() {
            return userServices.fimd();
        }

        

        @PostMapping("/register")
        
            public String registerUser(@RequestParam String username, 
                                        @RequestParam String email, 
                                        @RequestParam String password) {
            
                
                User user = new User(username, email, password);

                String encodedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(encodedPassword);

                userServices.createUser(user);
                return "redirect:/login.html";
        
            }



              // @PostMapping("/register")
        // public String createUser(@RequestBody User user, HttpServletResponse response) {
        //     String encodedPassword = passwordEncoder.encode(user.getPassword());
        //     user.setPassword(encodedPassword);
        //     userServices.createUser(user);

        //     String jwt = jwtUtil.generateToken(user.getUsername());
        //     response.setHeader("Authorization", "Bearer " + jwt);

        //     return "success";
        // }

        // Get All Users
        


                        // @PostMapping("/postman")
            // @ResponseBody
            // public String login1(@RequestBody User user, HttpServletResponse response) {
            //     try {
            //         Authentication authentication = authenticationManager.authenticate(
            //             new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            //         );
            
            //         // Check if authentication was successful
            //         if (authentication.isAuthenticated()) {
            //             System.out.println("JWT generated successfully: " );

            //             String jwt = jwtUtil.generateToken(user.getUsername());

            //             response.setHeader("Authorization", "Bearer " + jwt);
            //             return "hello " + jwt;
            //         } else {
            //             return "failed: Authentication was unsuccessful.";
            //         }
            //     } catch (Exception e) {
            //         e.printStackTrace(); // Log the full stack trace
            //         return "failed: " + e.getMessage();
            //     }
            // }


}
