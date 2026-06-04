package com.prueba.consultorioMedico.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class SecretaryDto extends UserDto{
    private LocalTime start;
    private LocalTime end;
}
