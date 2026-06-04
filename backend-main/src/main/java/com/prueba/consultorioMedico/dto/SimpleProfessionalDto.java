package com.prueba.consultorioMedico.dto;

import com.prueba.consultorioMedico.enums.AccountStateEnum;
import com.prueba.consultorioMedico.enums.RoleEnum;

import lombok.*;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SimpleProfessionalDto {
    private String dni;
    private String name;
    private String lastname;
    private String address;
    private String email;
    private String phoneNumber;
    private String username;
    private String password;
    private RoleEnum role;
    private LocalTime start;
    private LocalTime end;
    private AccountStateEnum accountState;
}
