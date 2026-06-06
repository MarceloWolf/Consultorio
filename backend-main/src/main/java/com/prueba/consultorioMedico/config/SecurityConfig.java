package com.prueba.consultorioMedico.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
        private final AuthenticationProvider authProvider;
        private final JwtAuthFilter jwtAuthFilter;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http.csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(authRequests -> authRequests
                                .requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .requestMatchers("/api/speciality/**").hasAnyRole("ADMIN", "SECRETARY", "PROFESSIONAL")
                                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/secretary/**").hasAnyRole("ADMIN", "SECRETARY", "PROFESSIONAL")
                                .requestMatchers("/api/secretary/**").hasAnyRole("ADMIN", "SECRETARY")
                                .requestMatchers("/api/users/user/findByUsername/**", "/api/users/user/findByDni/**", "/api/users/updateUserPassword/**")
                                .hasAnyRole("ADMIN", "SECRETARY", "PROFESSIONAL")
                                .requestMatchers("/api/users/PROFESSIONAL", "/api/users/SECRETARY").hasAnyRole("ADMIN", "SECRETARY")
                                .requestMatchers("/api/users/**").hasRole("ADMIN")
                                .requestMatchers("/api/appointment/**").hasAnyRole("ADMIN", "SECRETARY", "PROFESSIONAL")
                                .requestMatchers("/api/medical-records/**")
                                .hasAnyRole("ADMIN", "PROFESSIONAL")
                                .requestMatchers("/api/consultations/**")
                                .hasAnyRole("ADMIN", "PROFESSIONAL")
                                .requestMatchers("/api/professional/**")
                                .hasAnyRole("ADMIN", "PROFESSIONAL") 
                                 .anyRequest().authenticated())
                .sessionManagement(sessionManagement -> sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
               .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); 
                return http.build();
        }

}
