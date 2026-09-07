package com.auth.controller;

import com.auth.exception.BadRequestException;
import com.auth.model.dto.JwtTokenResponse;
import com.auth.model.dto.LoginRequestDto;
import com.auth.model.dto.UserDto;
import com.auth.model.entity.User;
import com.auth.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody User user){
        UserDto userDto = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtTokenResponse>  login(@RequestBody LoginRequestDto loginRequestDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));
        try {
            if (authentication.isAuthenticated()) {
                JwtTokenResponse jwtTokenResponse = userService.generateToken(loginRequestDto.getEmail());
                return ResponseEntity.ok(jwtTokenResponse);
            } else {
                throw new BadRequestException("Invalid email or password");
            }
        }catch (Exception e){
            throw new BadRequestException("Invalid email or password");
        }
    }
}


