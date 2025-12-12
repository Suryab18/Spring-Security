package com.example.SpringSecurity.Service;


import com.example.SpringSecurity.Model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class JwtService {


    @Value("${jwt.secret}")
    private String jwtSecret;

    public String generateToken(User user) {
        HashMap<String, Object> claims = new HashMap<>();

        claims.put("username", user.getName());
        claims.put("id", user.getId());
        claims.put("password", user.getPassword());
        claims.put("roles", List.of("ROLE_USER"));

        String jwtToken =  Jwts
                .builder()
                .setClaims(claims)
                .setSubject(user.getName())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() * 1000 * 60 * 60))
                .signWith(generateKey())
                .compact();

        System.out.println(jwtToken);

        return jwtToken;
    }

    public SecretKey generateKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public UserDetails getUserDetails(String token, UserDetailsService userDetailsService) {
        Claims claims = Jwts
                .parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();


        String username = claims.getSubject();

        return userDetailsService.loadUserByUsername(username);
    }

    public boolean validateToken(String token, UserDetailsService userDetailsService) {
        try{
            Claims claims = Jwts
                    .parser()
                    .verifyWith(generateKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();


            String username = claims.getSubject();
            Date expiration = claims.getExpiration();

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//            System.out.println("User details");
//            System.out.println(userDetails.getUsername() + " " + username);

            if(username.equals(userDetails.getUsername())) {
//                System.out.println(expiration + " " +new Date());
                return expiration.after(new Date());
            }

            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
