package com.test.controller;

import com.test.security.JwtTokenUtil;
import com.test.dtos.ResponseGenericDTO;
import com.test.dtos.UserDto;
import com.test.exceptions.UserRuntimeException;
import com.test.model.*;
import com.test.service.JwtUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtAuthenticationController {


    private final AuthenticationManager authenticationManager;


    private final JwtTokenUtil jwtTokenUtil;


    private final JwtUserService userDetailsService;

    public JwtAuthenticationController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, JwtUserService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping(value = "/register")
    public ResponseGenericDTO saveUser(@RequestBody UserDto user) {
        return userDetailsService.save(user);
    }

    private void authenticate(String username, String password) throws UserRuntimeException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new UserRuntimeException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new UserRuntimeException("INVALID_CREDENTIALS", e);
        }
    }
}
