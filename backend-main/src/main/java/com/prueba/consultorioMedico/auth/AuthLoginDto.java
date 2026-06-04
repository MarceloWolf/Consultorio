package com.prueba.consultorioMedico.auth;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthLoginDto {
    private String username;
    private String password;
}
