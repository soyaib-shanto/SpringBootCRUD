package com.example.loginfinal.Services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.loginfinal.Model.User;
import com.example.loginfinal.Repositary.UserRepository;

import java.util.List;


@Service
public class UserServices {
    
      @Autowired
      private UserRepository userRepository;

    // Create a new user
    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> fimd() {
        return userRepository.findAll();
    }
}
