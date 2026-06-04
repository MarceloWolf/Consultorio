package com.prueba.consultorioMedico.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

import com.prueba.consultorioMedico.enums.MedicalAppointmentStateEnum;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
//Dto con el objetivo de mostrar informacion
public class FullMedicalAppointmentDto {
    Long medicalAppointmentId;
    String specialityName;
    String professionalDni;
    String professionalName;
    String professionalLastname;
    String secretaryDni;
    String patientDni;
    String patientName;
    String patientLastname;
    MedicalAppointmentStateEnum state;
    LocalDate date;
    LocalTime time;
}
