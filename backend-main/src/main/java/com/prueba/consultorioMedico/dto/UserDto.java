package com.prueba.consultorioMedico.dto;

import com.prueba.consultorioMedico.enums.AccountStateEnum;
import com.prueba.consultorioMedico.enums.RoleEnum;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class UserDto {
    @NotBlank(message = "El DNI es obligatorio.")
    @Size(max = 10, message = "El DNI no puede superar los 20 caracteres.")
    private String dni;

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 50, message = "El nombre no puede superar los 50 caracteres.")
    private String name;

    @NotBlank(message = "El apellido es obligatorio.")
    @Size(max = 50, message = "El apellido no puede superar los 50 caracteres.")
    private String lastname;

    @NotBlank(message = "La dirección es obligatoria.")
    @Size(max = 100, message = "La dirección no puede superar los 100 caracteres.")
    private String address;

    @NotBlank(message = "El correo electrónico es obligatorio.")
    @Email(message = "El correo electrónico debe tener un formato válido.")
    @Size(max = 100, message = "El correo electrónico no puede superar los 100 caracteres.")
    private String email;

    @NotBlank(message = "El número de teléfono es obligatorio.")
    @Pattern(regexp = "\\+?[0-9]+", message = "El número de teléfono debe contener solo dígitos.")
    @Size(max = 15, message = "El número de teléfono no puede superar los 15 caracteres.")
    private String phoneNumber;

    @NotBlank(message = "El nombre de usuario es obligatorio.")
    @Size(max = 50, message = "El nombre de usuario no puede superar los 50 caracteres.")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(max = 100, message = "La contraseña no puede superar los 100 caracteres.")
    private String password;

    private RoleEnum role;

    private boolean newAccount;

    @NotNull(message = "El estado de la cuenta es obligatorio.")
    private AccountStateEnum accountState;


    public UserDto() {
        this.newAccount = true;
        this.accountState = AccountStateEnum.ACTIVE;
    }

}
