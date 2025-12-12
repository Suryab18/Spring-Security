package com.example.SpringSecurity.Service;

import com.example.SpringSecurity.DAO.UserRepository;
import com.example.SpringSecurity.Model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    
    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

}
