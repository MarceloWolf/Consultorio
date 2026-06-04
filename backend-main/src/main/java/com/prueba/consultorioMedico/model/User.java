package com.prueba.consultorioMedico.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.prueba.consultorioMedico.enums.AccountStateEnum;
import com.prueba.consultorioMedico.enums.RoleEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    @NotBlank(message = "El DNI es obligatorio.")
    @Pattern(regexp = "\\d{7,8}", message = "El DNI debe ser un número de 7 u 8 dígitos.")
    @Size(max = 10, message = "El DNI no puede superar los 20 caracteres.")
    private String dni;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 50, message = "El nombre no puede superar los 50 caracteres.")
    private String name;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "El apellido es obligatorio.")
    @Size(max = 50, message = "El apellido no puede superar los 50 caracteres.")
    private String lastname;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "La dirección es obligatoria.")
    @Size(max = 100, message = "La dirección no puede superar los 100 caracteres.")
    private String address;

    @Column(nullable = false, unique = true, length = 100)
    @NotBlank(message = "El correo electrónico es obligatorio.")
    @Email(message = "El correo electrónico debe tener un formato válido.")
    @Size(max = 100, message = "El correo electrónico no puede superar los 100 caracteres.")
    private String email;

    @Column(nullable = false, length = 15,name = "phone_number")
    @NotBlank(message = "El número de teléfono es obligatorio.")
    @Pattern(regexp = "\\+?[0-9]+", message = "El número de teléfono debe contener solo dígitos.")
    @Size(max = 15, message = "El número de teléfono no puede superar los 15 caracteres.")
    private String phoneNumber;

    @Column(nullable = false, unique = true, length = 50)
    @NotBlank(message = "El nombre de usuario es obligatorio.")
    @Size(max = 50, message = "El nombre de usuario no puede superar los 50 caracteres.")
    private String username;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(max = 100, message = "La contraseña no puede superar los 100 caracteres.")
    private String password;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @Column(nullable = false,name = "new_account")
    private boolean newAccount;

    @Column(nullable = false,name = "account_state")
    @Enumerated(EnumType.STRING)
    /* @NotNull(message = "El estado de la cuenta es obligatorio.") */
    private AccountStateEnum accountState;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User() {
        this.newAccount = true;
        this.accountState = AccountStateEnum.ACTIVE;
    }
}
