package com.prueba.consultorioMedico.auth;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    public String token;
    public HttpStatus httpStatus;
    public String message;
}
