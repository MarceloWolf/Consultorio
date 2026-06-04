package com.prueba.consultorioMedico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.prueba.consultorioMedico.model.ProfessionalSpeciality;

import java.util.Optional;

public interface IProfessionalSpecialityRepository extends JpaRepository<ProfessionalSpeciality,Long> {
    Optional<ProfessionalSpeciality> findBySpecialityName(String specialityName);

}
