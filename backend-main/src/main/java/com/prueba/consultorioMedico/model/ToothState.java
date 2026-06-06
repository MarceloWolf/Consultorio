package com.prueba.consultorioMedico.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tooth_state", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"patient_id", "tooth_number"})
})
public class ToothState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", referencedColumnName = "dni", nullable = false)
    @NotNull(message = "El paciente no puede ser nulo.")
    private Patient patient;

    @Column(name = "tooth_number", nullable = false)
    @NotNull(message = "El número de diente no puede ser nulo.")
    private Integer toothNumber;

    @Column(nullable = false)
    @NotNull(message = "El estado del diente no puede ser nulo.")
    private String state;

    @Column(length = 500)
    private String notes;
}
