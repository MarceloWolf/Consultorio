package com.prueba.consultorioMedico.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="speciality")
public class Speciality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name =  "speciality_id")
    private Long specialityId;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    @NotBlank(message = "El nombre de la especialidad es obligatorio.")
    @Size(max = 100, message = "El nombre de la especialidad no puede superar los 100 caracteres.")
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "speciality", fetch = FetchType.LAZY, targetEntity = ProfessionalSpeciality.class)
    private Set<ProfessionalSpeciality> professionalList = new HashSet<>();
}
