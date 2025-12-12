package com.example.SpringSecurity.Configuration;

import com.example.SpringSecurity.Service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilterChain extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    JwtFilterChain(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authHead = request.getHeader("Authorization");


        if(authHead == null || !authHead.startsWith("Bearer")){
            filterChain.doFilter(request, response);
            return;
        }

        final String jwtHeader = authHead.substring(7);

        UserDetails ud  = jwtService.getUserDetails(jwtHeader, userDetailsService);
        System.out.println("username: "+ ud.getUsername());

        boolean isValidToken = jwtService.validateToken(jwtHeader, userDetailsService);
        System.out.println("Is valid : "+ isValidToken);

        if(isValidToken) {
            UserDetails userDetails  = jwtService.getUserDetails(jwtHeader, userDetailsService);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            authenticationToken.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request)
            );

             SecurityContextHolder.getContext()
                    .setAuthentication(authenticationToken);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("Authentication in Context: " + authentication);

        }

        filterChain.doFilter(request, response);
    }
}
