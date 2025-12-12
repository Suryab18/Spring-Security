package com.example.SpringSecurity.Service;

import com.example.SpringSecurity.DAO.UserRepository;
import com.example.SpringSecurity.Model.User;
import com.example.SpringSecurity.Utils.CustomUserDetails;
import org.springframework.security.config.Customizer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByName(username);

        if(user == null) {
            System.out.println("User not found");
            throw new UsernameNotFoundException("User not found");
        }

        return new CustomUserDetails(user);
    }
}
