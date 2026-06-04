package com.prueba.consultorioMedico.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name="professional")
public class Professional extends User {

    @OneToMany(mappedBy = "professional", fetch = FetchType.LAZY, targetEntity = ProfessionalSpeciality.class)
    private Set<ProfessionalSpeciality> specialityList = new HashSet<>();

    @OneToMany(mappedBy = "professional", fetch = FetchType.LAZY, targetEntity = Consultation.class)
    private Set<Consultation> consultationList = new HashSet<>();

    @Column(nullable = false)
    @NotNull(message = "El horario de inicio es obligatorio.")
    private LocalTime start;

    @Column(nullable = false)
    @NotNull(message = "El horario de finalización es obligatorio.")
    private LocalTime end;

    @OneToMany(mappedBy = "professional", fetch = FetchType.LAZY, targetEntity = BusinessDays.class)
    private Set<BusinessDays> businessDaysList;
}
