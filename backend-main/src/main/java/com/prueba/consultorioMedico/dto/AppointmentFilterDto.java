package com.prueba.consultorioMedico.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AppointmentFilterDto {
    String specialityName;
    String professionalDni;
    LocalDate selectedDate;
}
