package com.prueba.consultorioMedico.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {
    private HttpStatus status;
    private String message;
}
