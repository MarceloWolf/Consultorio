package com.prueba.consultorioMedico.service.businessDays;

import com.prueba.consultorioMedico.dto.BusinessDaysDto;
import com.prueba.consultorioMedico.dto.ShiftDto;
import com.prueba.consultorioMedico.model.BusinessDays;
import com.prueba.consultorioMedico.model.Professional;
import com.prueba.consultorioMedico.model.Shift;
import com.prueba.consultorioMedico.repository.IBusinessDaysRepository;
import com.prueba.consultorioMedico.repository.IShiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusinessDaysService implements IBusinessDays {
    private final IBusinessDaysRepository businessDaysRepository;
    private final IShiftRepository shiftRepository;

    @Override
    @Transactional
    public Set<BusinessDays> createBusinessDays(Set<BusinessDaysDto> businessDays, Professional professional) {
        return businessDays.stream()
                .map(businessDaysDto -> {
                    BusinessDays daysSaved = BusinessDays.builder()
                            .day(businessDaysDto.getDayOfWeek())
                            .professional(professional)
                            .build();
                    businessDaysRepository.save(daysSaved);
                    generateShifts(daysSaved ,professional.getStart() ,professional.getEnd());
                    return daysSaved;
                })
                .collect(Collectors.toSet());
    }

    @Override
    public BusinessDaysDto toDto(BusinessDays businessDays) {
        return BusinessDaysDto.builder()
                .dayOfWeek(businessDays.getDay())
                .shifts(toShiftsDto(businessDays.getShift()))
                .build();
    }

    private List<ShiftDto> toShiftsDto(List<Shift> shifts) {
        return shifts.stream()
                .map(shift -> ShiftDto.builder()
                        .shiftTime(shift.getShiftTime())
                        .shiftDate(shift.getDate())
                        .isShiftReserved(shift.isShiftReserved())
                        .shiftDate(shift.getDate())
                        .build()
                )
                .collect(Collectors.toList());
    }

    private void generateShifts(BusinessDays businessDaysSaved, LocalTime start, LocalTime end) {
        int shiftDuration = 30; // Duración en minutos
        YearMonth currentMonth = YearMonth.now(); // Obtiene el mes y año actual
        DayOfWeek targetDayOfWeek = businessDaysSaved.getDay(); // Día laboral (ej. Lunes, Martes, etc.)

        // Iteramos sobre cada día del mes actual
        for (int day = 1; day <= currentMonth.lengthOfMonth(); day++) {
            LocalDate date = LocalDate.of(currentMonth.getYear(), currentMonth.getMonth(), day);

            // Si la fecha corresponde al día laboral
            if (date.getDayOfWeek() == targetDayOfWeek) {
                LocalTime actualShift = start;

                while (!actualShift.plusMinutes(shiftDuration).isAfter(end)) {
                    Shift shift = new Shift();
                    shift.setShiftTime(actualShift);
                    shift.setShiftReserved(false);
                    shift.setBusinessDays(businessDaysSaved);
                    shift.setDate(date); // Agregar la fecha del turno

                    shiftRepository.save(shift);
                    actualShift = actualShift.plusMinutes(shiftDuration);
                }
            }
        }
    }
}
