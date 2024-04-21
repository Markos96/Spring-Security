package com.curso.springsecurity.service;

import com.curso.springsecurity.data.dto.AuthenticationRequest;
import com.curso.springsecurity.data.dto.AuthenticationResponse;
import com.curso.springsecurity.data.entity.User;
import com.curso.springsecurity.data.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthenticationResponse login(AuthenticationRequest authenticationRequest){
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(),
                authenticationRequest.getPassword()
        );

        authenticationManager.authenticate(authToken);

        User user = userRepository.findByUsername(authenticationRequest.getUsername()).get();

        String jwt = jwtService.generateToken(user, generateClaims(user));

        return new AuthenticationResponse(jwt);
    }

    private Map<String, Object> generateClaims(User user) {

        //Add additional claims
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", user.getName());
        extraClaims.put("role", user.getRole());
        extraClaims.put("permissions", user.getAuthorities());

        return extraClaims;
    }
}
