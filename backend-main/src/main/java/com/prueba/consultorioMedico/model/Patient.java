package com.prueba.consultorioMedico.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Getter @Setter
@AllArgsConstructor
@Builder
@Table(name="patient")
public class Patient {
    @Id
    @Column(nullable = false, unique = true)
    @NotBlank(message = "El DNI no puede estar vacío.")
    @Size(max = 10, message = "El DNI no puede superar los 20 caracteres.")
    @Pattern(regexp = "\\d{7,8}", message = "El DNI debe ser un número de 7 u 8 dígitos.")
    private String dni;

    @Column(nullable = false)
    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 50, message = "El nombre no puede superar los 50 caracteres.")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "El apellido no puede estar vacío.")
    @Size(max = 50, message = "El nombre no puede superar los 50 caracteres.")
    private String lastname;

    @Column(nullable = false)
    @NotBlank(message = "La dirección no puede estar vacía.")
    @Size(max = 100, message = "La dirección no puede superar los 100 caracteres.")
    private String address;

    @Column(nullable = false)
    @Email(message = "El correo debe tener un formato válido.")
    @Size(max = 100, message = "La dirección no puede superar los 100 caracteres.")
    private String email;

    @Column(nullable = false,name = "phone_number")
    @Pattern(regexp = "\\+?[0-9]+", message = "El número de teléfono debe contener solo dígitos.")
    @Size(max = 15, message = "El número de teléfono no puede superar los 15 caracteres.")
    private String phoneNumber;

    @Column(nullable = false)
    @NotNull(message = "La fecha de nacimiento no puede ser nula.")
    private LocalDate birthdate;

    private boolean active;

    @Valid
    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private MedicalRecord medicalRecord;

    public Patient() {
        this.active = true;
    }
}