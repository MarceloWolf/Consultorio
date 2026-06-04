package com.prueba.consultorioMedico.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "consultation")
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_record_id", nullable = false)
    @NotNull(message = "El registro médico no puede ser nulo.")
    private MedicalRecord medicalRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professional_id", nullable = false)
    @NotNull(message = "El profesional no puede ser nulo.")
    private Professional professional;

    @Column(nullable = false,name = "speciality_name")
    @NotBlank(message = "El nombre de la especialidad no puede estar vacío.")
    private String specialityName;

    @Column(nullable = false)
    @NotNull(message = "La fecha no puede ser nula.")
    private LocalDate date;

    @Column(nullable = false)
    @NotNull(message = "La hora no puede ser nula.")
    private LocalTime time;

    @Column(nullable = false)
    @NotBlank(message = "El motivo no puede estar vacío.")
    private String reason;

    @Column(nullable = false)
    @NotBlank(message = "El diagnóstico no puede estar vacío.")
    private String diagnosis;

    @Column(nullable = false)
    @NotBlank(message = "El tratamiento no puede estar vacío.")
    private String treatment;
}
