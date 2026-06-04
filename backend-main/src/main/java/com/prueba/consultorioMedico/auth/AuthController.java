package com.prueba.consultorioMedico.auth;

import com.prueba.consultorioMedico.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody UserDto userDto) {
        AuthResponse authResponse = authService.register(userDto);
        AuthResponse response = AuthResponse.builder()
                .token(authResponse.getToken())
                .message("Register successful")
                .httpStatus(HttpStatus.OK)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthLoginDto userDto){
        AuthResponse authResponse = authService.authenticate(userDto);
        AuthResponse response = AuthResponse.builder()
                .token(authResponse.getToken())
                .message("Login successful")
                .httpStatus(HttpStatus.OK)
                .build();
        return ResponseEntity.ok(response);
    }




}
