package com.prueba.consultorioMedico.model;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name="secretary")
public class Secretary extends User {

    @Column(nullable = false)
    @NotNull(message = "El horario de inicio es obligatorio.")
    private LocalTime start;

    @Column(nullable = false)
    @NotNull(message = "El horario de finalización es obligatorio.")
    private LocalTime end;

}
