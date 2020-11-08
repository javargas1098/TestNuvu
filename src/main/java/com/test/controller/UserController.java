package com.test.controller;

import com.test.dtos.ResponseGenericDTO;
import com.test.dtos.UserDetailDto;
import com.test.service.JwtUserService;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private final JwtUserService userDetailsService;

    public UserController(JwtUserService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/users")
    public ResponseGenericDTO getEmployeeById(@RequestParam(name = "id") long id) {
        return userDetailsService.getUsers(id);
    }

    @PutMapping(value = "/update")
    public ResponseGenericDTO updateUser(@RequestBody UserDetailDto userDetailDto) {
        return userDetailsService.update(userDetailDto);
    }


    @DeleteMapping("/users/id")
    public ResponseGenericDTO deleteUserById(@RequestParam(name = "id") long id) {
        return userDetailsService.deleteUserById(id);
    }
}