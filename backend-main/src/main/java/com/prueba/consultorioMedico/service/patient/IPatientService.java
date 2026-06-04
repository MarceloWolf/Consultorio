package com.prueba.consultorioMedico.service.patient;

import java.util.List;

import com.prueba.consultorioMedico.dto.PatientDto;
import com.prueba.consultorioMedico.exception.PatientAlreadyExistException;
import com.prueba.consultorioMedico.model.Patient;

public interface IPatientService {
    List<Patient> findAll();
    Patient findByDNI(String dni);
    Patient findByLastName(String lastName);
    void add(Patient patient) throws PatientAlreadyExistException;
    void update(PatientDto patient, String dni);
    void updateState(String dni, Boolean newState);
    List<Patient> findAllByActiveStatus(Boolean activeStatus);
}
