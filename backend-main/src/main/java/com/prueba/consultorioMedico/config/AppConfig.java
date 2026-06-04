package com.prueba.consultorioMedico.config;

import com.prueba.consultorioMedico.model.Admin;
import com.prueba.consultorioMedico.model.AdminUser;
import com.prueba.consultorioMedico.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final IUserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            var existingAdmin = userRepository.findAll().stream()
                    .filter(u -> u instanceof Admin)
                    .map(u -> (Admin) u)
                    .findFirst();
            if (existingAdmin.isPresent()) {
                Admin admin = existingAdmin.get();
                var adminUserMatch = admin.getUsers().stream()
                        .filter(adminUser -> adminUser.getUsername().equals(username))
                        .findFirst();

                if (adminUserMatch.isPresent()) {
                    AdminUser adminUser = adminUserMatch.get();
                    return new org.springframework.security.core.userdetails.User(
                            adminUser.getUsername(),
                            adminUser.getPassword(),
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
                }
            }

            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
