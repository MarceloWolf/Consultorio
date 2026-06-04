package com.prueba.consultorioMedico.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter @Setter
@AllArgsConstructor
@Builder
@Table(name="medical_record")
@Entity
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "notes")
    private String description;

    @OneToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "dni", nullable = false)
    @NotNull(message = "El paciente no puede ser nulo.")
    private Patient patient;

    @Column(nullable = false)
    @NotNull(message = "La fecha no puede ser nula.")
    private LocalDate date;

    @Column(nullable = false)
    @NotNull(message = "La hora no puede ser nula.")
    private LocalTime time;

    @Column(precision = 3, scale = 2)
    @Positive
    @Digits(integer = 3, fraction = 2, message = "La altura debe tener un formato válido.")
    private BigDecimal height;

    @Column(precision = 5, scale = 2)
    @Positive
    @Digits(integer = 5, fraction = 2, message = "El peso debe tener un formato válido.")
    private BigDecimal weight;

    @Column(length = 3)
    private String bloodGroup;

    public MedicalRecord() {
        this.date = LocalDate.now();
        this.time = LocalTime.now();
    }
}
