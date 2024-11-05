package com.example.loginfinal.Model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;


public class ImpUserDetails implements UserDetails {

    private User user;

    public ImpUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Returning a collection of roles or authorities for the user
       return null;
      //return Collections.singleton(new SimpleGrantedAuthority("USER"));
       //singleton(new SimpleGrantedAuthority("user"));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername(); // In this case, the email is used as the username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // You can implement your logic here for account expiration
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Implement logic to check if the account is locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // You can add credential expiration checks if necessary
    }

    @Override
    public boolean isEnabled() {
        return true; // Implement your own logic to check if the account is enabled
    }
}

