package com.prueba.consultorioMedico.repository;

import com.prueba.consultorioMedico.enums.RoleEnum;
import com.prueba.consultorioMedico.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByDni(String username);

    List<User> findAllByRole(RoleEnum role);
}
