package com.prueba.consultorioMedico.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ShiftDto {
    private LocalTime shiftTime;
    private LocalDate shiftDate;
    private boolean isShiftReserved;
}
