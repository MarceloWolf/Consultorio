package com.prueba.consultorioMedico.dto;

import java.time.LocalDate;
import java.time.LocalTime;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicalAppointmentDataAllowedToUpdateDto {
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
}
