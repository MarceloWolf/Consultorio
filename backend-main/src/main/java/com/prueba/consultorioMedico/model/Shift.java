package com.prueba.consultorioMedico.model;

import java.time.LocalDate;
import java.time.LocalTime;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Entity
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    @NotNull(message = "El horario del turno es obligatorio.")
    private LocalTime shiftTime;

    @Column(nullable = false,name = "is_shift_reserved")
    private boolean isShiftReserved;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "business_days_id", nullable = false)
    @NotNull(message = "El día de negocio es obligatorio.")
    private BusinessDays businessDays;

    public Shift() {
        this.isShiftReserved = false;
    }
}