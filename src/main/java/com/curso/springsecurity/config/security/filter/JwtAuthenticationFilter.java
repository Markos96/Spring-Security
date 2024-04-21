package com.curso.springsecurity.config.security.filter;

import com.curso.springsecurity.data.entity.User;
import com.curso.springsecurity.data.respository.UserRepository;
import com.curso.springsecurity.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1 - Get header contained in JWT
        String authHeader = request.getHeader("Authorization");

        // 2 - Get JWT of header
        if(Objects.isNull(authHeader) || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.split(" ")[1];

        // 3 - Get username/subject of JWT
        String username = jwtService.extractUsername(jwt);

        // 4 - Set object Authentication into SecurityContext

        User user = userRepository.findByUsername(username).get();

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 5 - Execute filter

        filterChain.doFilter(request, response);

    }
}
