package com.example.loginfinal.Services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.loginfinal.Model.ImpUserDetails;
import com.example.loginfinal.Model.User;
import com.example.loginfinal.Repositary.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        // Fetch user from MySQL using UserRepository
         User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + name));

              
        return new ImpUserDetails(user);
  }
}
