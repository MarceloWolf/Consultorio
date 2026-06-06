package com.prueba.consultorioMedico.repository;

import com.prueba.consultorioMedico.model.ToothState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IToothStateRepository extends JpaRepository<ToothState, Long> {
    List<ToothState> findByPatientDni(String patientDni);
    Optional<ToothState> findByPatientDniAndToothNumber(String patientDni, Integer toothNumber);
}
