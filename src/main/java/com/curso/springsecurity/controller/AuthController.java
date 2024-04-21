package com.curso.springsecurity.controller;

import com.curso.springsecurity.data.dto.AuthenticationRequest;
import com.curso.springsecurity.data.dto.AuthenticationResponse;
import com.curso.springsecurity.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @PreAuthorize("permitAll")
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest authenticationRequest){
        AuthenticationResponse jwtDTO = authenticationService.login(authenticationRequest);
        return ResponseEntity.ok(jwtDTO);
    }

    @PreAuthorize("permiteAll")
    @GetMapping("/public-access")
    public String publicAcess(){
        return "Spring Security Implement";
    }

}
