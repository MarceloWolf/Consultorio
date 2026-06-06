package com.prueba.consultorioMedico.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ToothStateDto {
    private Integer toothNumber;
    private String state;
    private String notes;
}
