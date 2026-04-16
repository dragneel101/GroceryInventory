package com.grocery.service;

import com.grocery.model.AppUser;
import com.grocery.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AppUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return User.withUsername(user.getUsername())
            .password(user.getPassword())
            .roles(user.getRole().replace("ROLE_", ""))
            .build();
    }
}
