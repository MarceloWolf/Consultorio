package com.prueba.consultorioMedico.auth;

import com.prueba.consultorioMedico.config.JwtService;
import com.prueba.consultorioMedico.dto.UserDto;
import com.prueba.consultorioMedico.enums.AccountStateEnum;
import com.prueba.consultorioMedico.enums.RoleEnum;
import com.prueba.consultorioMedico.model.Admin;
import com.prueba.consultorioMedico.model.AdminUser;
import com.prueba.consultorioMedico.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;


    public AuthResponse register(UserDto request) {
        var existingAdmin = userRepository.findAll().stream()
                .filter(u -> u instanceof Admin)
                .map(u -> (Admin) u)
                .findFirst();
        Admin admin = existingAdmin.orElseGet(() -> new Admin());
        admin.setAccountState(AccountStateEnum.ACTIVE);
        admin.setRole(RoleEnum.ADMIN);
        admin.setEmail(request.getEmail());
        admin.setAddress(request.getAddress());
        admin.setPhoneNumber(request.getPhoneNumber());
        admin.setName(request.getName());
        admin.setLastname(request.getLastname());
        admin.setDni(request.getDni());
        boolean addedUser = admin.addUser(request.getUsername(), passwordEncoder.encode(request.getPassword()));
        if(!addedUser){
            throw new NoSuchElementException("No se puede agregar mas de 2 admins");
        }
        admin.setUsername(request.getUsername());;
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(admin);
        var jwtToken = jwtService.generateToken(admin);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthResponse authenticate(AuthLoginDto request) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var existingAdmin = userRepository.findAll().stream()
                .filter(u -> u instanceof Admin)
                .map(u -> (Admin) u)
                .findFirst();

        if (existingAdmin.isPresent()) {
            Admin admin = existingAdmin.get();
            var userMatch = admin.getUsers().stream()
                    .filter(adminUser -> adminUser.getUsername().equals(request.getUsername()))
                    .findFirst();

            if (userMatch.isPresent()) {
                AdminUser adminUser = userMatch.get();
                if (passwordEncoder.matches(request.getPassword(), adminUser.getPassword())) {
                    var jwtToken = jwtService.generateToken(admin);
                    return AuthResponse.builder()
                            .token(jwtToken)
                            .build();
                }
                throw new IllegalArgumentException("Contraseña Incorrecta");
            }
        }
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));
        var jwtToken = jwtService.generateToken(user);
        AuthResponse response = new AuthResponse();
        response.setToken(jwtToken);
        return response;
    }
}
