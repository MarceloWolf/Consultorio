package com.prueba.consultorioMedico.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicalRecordDto {

    @NotBlank(message = "La descripción es obligatoria.")
    @Size(max = 200, message = "La descripción no puede superar los 200 caracteres.")
    private String description;

    @NotNull(message = "La altura es obligatoria.")
    @Positive
    @Digits(integer = 3, fraction = 2, message = "La altura debe tener un formato válido con hasta 2 decimales.")
    private BigDecimal height;

    @NotNull(message = "El peso es obligatorio.")
    @Positive
    @Digits(integer = 3, fraction = 2, message = "El peso debe tener un formato válido con hasta 2 decimales.")
    private BigDecimal weight;

    @NotBlank(message = "El grupo sanguíneo es obligatorio.")
    @Size(max = 10, message = "El grupo sanguíneo no puede superar los 10 caracteres.")
    private String bloodGroup;
}
