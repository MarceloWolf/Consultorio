package com.prueba.consultorioMedico.service.user;

import com.prueba.consultorioMedico.auth.AuthResponse;
import com.prueba.consultorioMedico.config.JwtService;
import com.prueba.consultorioMedico.dto.FullProfessionalDto;
import com.prueba.consultorioMedico.dto.SecretaryDto;
import com.prueba.consultorioMedico.dto.SpecialityDto;
import com.prueba.consultorioMedico.dto.UserDto;
import com.prueba.consultorioMedico.enums.AccountStateEnum;
import com.prueba.consultorioMedico.enums.RoleEnum;
import com.prueba.consultorioMedico.model.BusinessDays;
import com.prueba.consultorioMedico.model.Professional;
import com.prueba.consultorioMedico.model.Secretary;
import com.prueba.consultorioMedico.model.User;
import com.prueba.consultorioMedico.repository.IUserRepository;
import com.prueba.consultorioMedico.service.businessDays.BusinessDaysService;
import com.prueba.consultorioMedico.service.profesionalSpeciality.IProfessionalSpecialityService;
import com.prueba.consultorioMedico.util.DateValidation;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final IUserRepository userRepository;
    private final IProfessionalSpecialityService professionalSpecialityService;
    private final BusinessDaysService businessDaysService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Transactional
    public List<UserDto> findAllUsersByRole(RoleEnum role) {
        List<User> users = userRepository.findAllByRole(role);
        /* return toDTOS(users); */
        return users.stream().map(user -> {
            UserDto userDto = UserDto.builder()
                    .dni(user.getDni())
                    .name(user.getName())
                    .lastname(user.getLastname())
                    .address(user.getAddress())
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .role(user.getRole())
                    .newAccount(user.isNewAccount())
                    .accountState(user.getAccountState())
                    .build();
            return userDto;
        }).toList();
    }

    @Override
    public UserDto findUserByDni(String dni) {
        User user = userRepository.findByDni(dni)
                .orElseThrow(() -> new NoSuchElementException("Usuario con DNI: " +
                dni + " no fue encontrado"));

        return UserDto.builder()
        .dni(user.getDni())
        .name(user.getName())
        .lastname(user.getLastname())
        .address(user.getAddress())
        .email(user.getEmail())
        .phoneNumber(user.getPhoneNumber())
        .username(user.getUsername())
        .password(user.getPassword())
        .role(user.getRole())
        .newAccount(user.isNewAccount())
        .accountState(user.getAccountState())
        .build();
    }

    @Override
    @Transactional
    public AuthResponse createSecretary(SecretaryDto secretaryDto) {
        Secretary secretary = secretaryToEntity(secretaryDto);
        userRepository.save(secretary);
        var jwtToken = jwtService.generateToken(secretary);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    @Transactional
    public AuthResponse createProfessional(FullProfessionalDto professionalDto, List<SpecialityDto> specilityNames) {
        // Si el tiempo del profesional esta dentro del tiempo de servicio
        DateValidation.validateProfessionalTimeOnService(professionalDto.getStart(), professionalDto.getEnd());
        // Si los tiempos del profesional son validos, o sea que el inicio no es despues
        // del fin.
        DateValidation.validateTime(professionalDto.getStart(), professionalDto.getEnd());

        Professional professional = professionalToEntity(professionalDto);
        Set<BusinessDays> businessDays = businessDaysService.createBusinessDays(professionalDto.getBusinessDays(),
                professional);
        professional.setBusinessDaysList(businessDays);
        userRepository.save(professional);

        professionalSpecialityService.addSpeciality(professional.getDni(), specilityNames);
        var jwtToken = jwtService.generateToken(professional);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    @Transactional
    public void reactivateUser(String dni)
    {
        User user = userRepository.findByDni(dni)
        .orElseThrow(() -> new NoSuchElementException("Usuario con DNI: " +
        dni + " no fue encontrado"));

        user.setAccountState(AccountStateEnum.ACTIVE);

        userRepository.save(user);
    }



    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> {
            UserDto userDto = UserDto.builder()
                    .dni(user.getDni())
                    .name(user.getName())
                    .lastname(user.getLastname())
                    .address(user.getAddress())
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .role(user.getRole())
                    .newAccount(user.isNewAccount())
                    .accountState(user.getAccountState())
                    .build();
            return userDto;
        }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Usuario con username: " +
        username + " no fue encontrado"));

        return UserDto.builder()
        .dni(user.getDni())
        .name(user.getName())
        .lastname(user.getLastname())
        .address(user.getAddress())
        .email(user.getEmail())
        .phoneNumber(user.getPhoneNumber())
        .username(user.getUsername())
        .password(user.getPassword())
        .role(user.getRole())
        .newAccount(user.isNewAccount())
        .accountState(user.getAccountState())
        .build();
    }

    @Override
    @Transactional
    public void updatePassword(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Usuario con DNI: " + username + " no fue encontrado"));

        if (user.isNewAccount()) // Lo realizo asi para que si el usuario se olvida su contraseña pueda
                                 // actualizarla cuando quiera
        {
            user.setNewAccount(false);
        }
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    private Professional professionalToEntity(FullProfessionalDto professionalDto) {
        return Professional.builder()
                .end(professionalDto.getEnd())
                .start(professionalDto.getStart())
                .username(professionalDto.getUsername())
                .name(professionalDto.getName())
                .lastname(professionalDto.getLastname())
                .email(professionalDto.getEmail())
                .dni(professionalDto.getDni())
                .address(professionalDto.getAddress())
                .phoneNumber(professionalDto.getPhoneNumber())
                .role(RoleEnum.PROFESSIONAL)
                .password(passwordEncoder.encode(professionalDto.getPassword()))
                .accountState(professionalDto.getAccountState())
                .newAccount(true)
                .build();
    }

    private Secretary secretaryToEntity(SecretaryDto secretaryDto) {
        return Secretary.builder()
                .name(secretaryDto.getName())
                .lastname(secretaryDto.getLastname())
                .dni(secretaryDto.getDni())
                .email(secretaryDto.getEmail())
                .address(secretaryDto.getAddress())
                .phoneNumber(secretaryDto.getPhoneNumber())
                .start(secretaryDto.getStart())
                .end(secretaryDto.getEnd())
                .username(secretaryDto.getUsername())
                .password(passwordEncoder.encode(secretaryDto.getPassword()))
                .role(RoleEnum.SECRETARY)
                .accountState(secretaryDto.getAccountState())
                .newAccount(true)
                .build();
    }
}
