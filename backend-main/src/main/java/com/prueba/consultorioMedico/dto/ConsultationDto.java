package com.prueba.consultorioMedico.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class ConsultationDto {
    @NotNull(message = "La fecha de consulta es obligatoria.")
    private LocalDate date;

    @NotNull(message = "La hora de consulta es obligatoria.")
    private LocalTime time;

    @NotBlank(message = "El nombre de la especialidad es obligatorio.")
    @Size(max = 50, message = "El nombre de la especialidad no puede superar los 50 caracteres.")
    private String specialityName;

    @NotBlank(message = "El motivo de consulta es obligatorio.")
    @Size(max = 200, message = "El motivo no puede superar los 200 caracteres.")
    private String reason;

    @NotBlank(message = "El diagnóstico es obligatorio.")
    private String diagnosis;

    @NotBlank(message = "El tratamiento es obligatorio.")
    private String treatment;
}
