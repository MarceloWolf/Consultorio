package com.prueba.consultorioMedico.controller;

import com.prueba.consultorioMedico.auth.AuthResponse;
import com.prueba.consultorioMedico.dto.*;
import com.prueba.consultorioMedico.enums.AccountStateEnum;
import com.prueba.consultorioMedico.enums.RoleEnum;
import com.prueba.consultorioMedico.model.Secretary;
import com.prueba.consultorioMedico.model.User;
import com.prueba.consultorioMedico.service.professional.ProfessionalService;
import com.prueba.consultorioMedico.service.secretary.SecretaryService;
import com.prueba.consultorioMedico.service.user.IUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private final IUserService userService;
    @Autowired
    private final ProfessionalService professionalService;
    @Autowired
    private final SecretaryService secretaryService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userRole}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getUsersByRole(@PathVariable String userRole) {
        RoleEnum role = RoleEnum.valueOf(userRole);
        return ResponseEntity.ok(userService.findAllUsersByRole(role));
    }

    @GetMapping("/user/findByUsername/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.findUserByUsername(username));
    }

    @GetMapping("/user/findByDni/{dni}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> getUserByDni(@PathVariable String dni) {
        return ResponseEntity.ok(userService.findUserByDni(dni));
    }

    @GetMapping("/professional/{accountStateEnum}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SimpleProfessionalDto>> getProfessionalsByAcountState(
            @PathVariable AccountStateEnum accountStateEnum) {
        return ResponseEntity.ok(professionalService.findAllByAccountState(accountStateEnum));
    }

    @GetMapping("/secretary/{accountStateEnum}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Secretary>> getSecretarysByActive(@PathVariable AccountStateEnum accountStateEnum) {
        return ResponseEntity.ok(secretaryService.findAllByAccountState(accountStateEnum));
    }

    @PostMapping("/addSecretary")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthResponse> addSecretary(@Valid @RequestBody SecretaryDto secretary) {
        AuthResponse authResponse = userService.createSecretary(secretary);
        AuthResponse response = AuthResponse.builder()
                .token(authResponse.getToken())
                .message("Secretario/a guardado/a correctamente")
                .httpStatus(HttpStatus.OK)
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addProfessionalWithSpecialities")
    public ResponseEntity<AuthResponse> addProfessional(@Valid @RequestBody ProfessionalSpecialitiesDTO pSpecialitiesDTO) {
        if (pSpecialitiesDTO.getSpecialityNames().isEmpty() || pSpecialitiesDTO.getSpecialityNames() == null) {
            AuthResponse message = AuthResponse.builder()
                    .message("Debe proporcionar por lo menos una especialidad")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
                } else {
            AuthResponse authResponse = userService.createProfessional(pSpecialitiesDTO.getProfessional(),
                    pSpecialitiesDTO.getSpecialityNames());
            AuthResponse response = AuthResponse.builder()
                    .token(authResponse.getToken())
                    .httpStatus(HttpStatus.OK)
                    .message("Professional/a guardado/a correctamente")
                    .build();
            return ResponseEntity.ok(response);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/secretary/{dni}/delete")
    public ResponseEntity<?> deleteSecretary(@PathVariable String dni) {
        try {
            if (dni.isEmpty() || !dni.matches("\\d{7,8}")) { 
                return ResponseEntity.badRequest().body("El DNI debe ser un número de 7 u 8 dígitos.");
            }
            secretaryService.deleteSecretary(dni);
            Message message = Message.builder().status(HttpStatus.OK).message("Secretario/a eliminado/a correctamente")
                    .build();
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El valor ingresado es incorrecto");
        } finally {
            log.info("Busqueda de secretario/a con DNI: " + dni + " finalizada.");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/professional/{dni}/delete")
    public ResponseEntity<?> deleteProfessional(@PathVariable String dni) {
        try {
            if (dni.isEmpty() || !dni.matches("\\d{7,8}")) { 
                return ResponseEntity.badRequest().body("El DNI debe ser un número de 7 u 8 dígitos.");
            }
            professionalService.deleteProfessionalByDNI(dni);
            Message message = Message.builder().status(HttpStatus.OK).message("Profesional/a eliminado/a correctamente")
                    .build();
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El valor ingresado es incorrecto");
        } finally {
            log.info("Busqueda de profesional con DNI: " + dni + " finalizada.");
        }
    }

    @PatchMapping("/updateUserPassword/{username}/{password}")
    @PreAuthorize("hasAnyRole('ADMIN','SECRETARY','PROFESSIONAL')")
    public ResponseEntity<?> updateUserPassword(@PathVariable String username, @PathVariable String password) {
        try {
            if (username.isEmpty()) { // Ajusta el patrón si es necesario
                return ResponseEntity.badRequest().body("El Username no debe estar vacio");
            }
            userService.updatePassword(username, password);
            Message message = Message.builder().status(HttpStatus.OK).message("Contraseña actualizada exitosamente.")
                    .build();
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El valor ingresado es incorrecto");
        } finally {
            log.info("Busqueda de user con Username: " + username + " finalizada.");
        }
    }

    @PatchMapping("/reactivateUser/{dni}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> reactivateUser(@PathVariable String dni) {
        try {
            if (dni.isEmpty()) { // Ajusta el patrón si es necesario
                return ResponseEntity.badRequest().body("El dni no debe estar vacio");
            }
            userService.reactivateUser(dni);
            Message message = Message.builder().status(HttpStatus.OK).message("Estado actualizado exitosamente.")
                    .build();
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El valor ingresado es incorrecto");
        } finally {
            log.info("Busqueda de user con Username: " + dni + " finalizada.");
        }
    }



    @PutMapping("/updateSecretary/{dni}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Message> updateSecretary(@Valid @RequestBody SecretaryDto secretary,
                                                   @PathVariable String dni) {
        secretaryService.updateSecretary(secretary, dni);
        Message message = Message.builder().status(HttpStatus.OK).message("Secretario/a actualizado correctamente")
                .build();
        return ResponseEntity.ok(message);
    }

    @PutMapping("/updateProfessional/{dni}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Message> updateProfessional(@Valid @RequestBody SimpleProfessionalDto professionalDto,
                                                      @PathVariable String dni) {
        professionalService.updateProfessional(professionalDto, dni);
        Message message = Message.builder().status(HttpStatus.OK).message("Profesional actualizado correctamente")
                .build();
        return ResponseEntity.ok(message);
    }

}
