package com.prueba.consultorioMedico.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
//Dto con el objetivo de recibir informacion
public class SimpleMedicalAppointmentDto {
    String professionalDni;
    String patientDni;
    String specialityName;
    String secretaryDni;
    LocalDate date;
    LocalTime time;
}
