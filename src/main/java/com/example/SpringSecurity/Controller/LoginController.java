package com.example.SpringSecurity.Controller;

import com.example.SpringSecurity.Model.User;
import com.example.SpringSecurity.Service.JwtService;
import com.example.SpringSecurity.Service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/login")
public class LoginController {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;

    LoginController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping
    public String login(@RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        String token = jwtService.generateToken(user);
        userService.saveUser(user);
        System.out.println("Generated Token: "+ token);
        return token;
    }

}
