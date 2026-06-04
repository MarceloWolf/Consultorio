package com.prueba.consultorioMedico.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.prueba.consultorioMedico.enums.AccountStateEnum;
import com.prueba.consultorioMedico.model.Secretary;

public interface ISecretaryRepository extends JpaRepository<Secretary,Long> {
    @Query("Select s from Secretary s where s.dni = ?1")
    Optional<Secretary> findByDni(String dni);
    List<Secretary> findAllByAccountState(AccountStateEnum accountStateEnum);
}
