package com.prueba.consultorioMedico.dto;

import lombok.*;

import java.time.DayOfWeek;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BusinessDaysDto {
    private DayOfWeek dayOfWeek;
    private List<ShiftDto> shifts;

    @JsonCreator
    public static BusinessDaysDto fromString(String dayOfWeekStr) {
        BusinessDaysDto dto = new BusinessDaysDto();
        dto.setDayOfWeek(DayOfWeek.valueOf(dayOfWeekStr));
        return dto;
    }
}
