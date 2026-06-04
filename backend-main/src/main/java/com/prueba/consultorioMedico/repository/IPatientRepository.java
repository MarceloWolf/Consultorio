package com.prueba.consultorioMedico.repository;

import com.prueba.consultorioMedico.model.Patient;

import java.util.List;
import java.util.Optional;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IPatientRepository extends JpaRepository<Patient,String> {

    @Query("Select p from Patient p where p.dni = ?1")
    Optional<Patient> findByDNI(String dni);

    List<Patient> findAllByActive(Boolean activeStatus);

    Patient findByLastname(String lastname);
}
