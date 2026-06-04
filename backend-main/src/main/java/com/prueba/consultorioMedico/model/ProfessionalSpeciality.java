package com.prueba.consultorioMedico.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "professional_speciality")
public class ProfessionalSpeciality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "professional_specialityId", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Professional.class)
    @JoinColumn(name = "professional_id", referencedColumnName = "id", nullable = false)
    @NotNull(message = "El profesional es obligatorio.")
    private Professional professional;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Speciality.class)
    @JoinColumn(name = "speciality_id", referencedColumnName = "speciality_id", nullable = false)
    @NotNull(message = "La especialidad es obligatoria.")
    private Speciality speciality;
}
