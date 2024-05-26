package com.example.calendar.Service;

import com.example.calendar.Entity.User;
import com.example.calendar.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email: "+ username));
        GrantedAuthority  authority;

        switch (user.getRole()) {
            case ROLE_ADMIN:
                authority = new SimpleGrantedAuthority("ROLE_ADMIN");
                break;
            case ROLE_USER:
                authority = new SimpleGrantedAuthority("ROLE_USER");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + user.getRole());
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                Collections.singleton(authority));

    }
}
