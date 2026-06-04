package com.prueba.consultorioMedico.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientDto {

    @NotBlank(message = "El DNI no puede estar vacío.")
    @Pattern(regexp = "\\d{7,8}")
    private String dni;

    @NotBlank(message = "El nombre no puede estar vacío.")
    private String name;

    @NotBlank(message = "El apellido no puede estar vacío.")
    private String lastname;

    @NotBlank(message = "La dirección no puede estar vacía.")
    private String address;

    @Email(message = "El correo debe tener un formato válido.")
    private String email;

    @Pattern(regexp = "\\+?[0-9]+", message = "El número de teléfono debe contener solo dígitos.")
    @Size(max = 15, message = "El número de teléfono no puede superar los 15 caracteres.")
    private String phoneNumber;

    @NotNull(message = "La fecha de nacimiento no puede ser nula.")
    private LocalDate birthdate;
}

