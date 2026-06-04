package com.prueba.consultorioMedico.service.patient;

import com.prueba.consultorioMedico.dto.PatientDto;
import com.prueba.consultorioMedico.exception.PatientAlreadyExistException;
import com.prueba.consultorioMedico.model.MedicalRecord;
import com.prueba.consultorioMedico.model.Patient;
import com.prueba.consultorioMedico.repository.IMedicalRecordRepository;
import com.prueba.consultorioMedico.repository.IPatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService implements IPatientService {
    private final IPatientRepository patientRepository;
    private final IMedicalRecordRepository medicalRecordRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    @Override
    @Transactional
    public void add(Patient patient) throws PatientAlreadyExistException {
        Optional<Patient> exisitinPatient = patientRepository.findByDNI(patient.getDni());

        if (exisitinPatient.isEmpty()) {
            MedicalRecord medicalRecord = new MedicalRecord();
            medicalRecord.setPatient(patient);
            patient.setMedicalRecord(medicalRecord);
            patientRepository.save(patient);
        }else {
            throw new PatientAlreadyExistException("Paciente con dni "+ patient.getDni() + " ya existe");
        }
    }


    @Override
    @Transactional(readOnly = true)
    public Patient findByDNI(String dni) {
        return patientRepository.findByDNI(dni)
                .orElseThrow(() -> new NoSuchElementException("Paciente con DNI: " +
                        dni + " no fue encontrado"));
    }

    @Override
    public Patient findByLastName(String lastName) {
        return patientRepository.findByLastname(lastName);
    }

    @Override
    @Transactional
    public void update(PatientDto patient, String dni) {
        Patient patientUpdated = patientRepository.findByDNI(dni)
                .orElseThrow(() -> new NoSuchElementException("Paciente con DNI: " +
                        dni + " no fue encontrado"));
        patientUpdated.setEmail(patient.getEmail());
        patientUpdated.setAddress(patient.getAddress());
        patientUpdated.setPhoneNumber(String.valueOf(patient.getPhoneNumber()));
        patientRepository.save(patientUpdated);
    }

    @Override
    @Transactional
    public void updateState(String dni, Boolean newState) {
        Patient patientUpdated = patientRepository.findByDNI(dni)
                .orElseThrow(() -> new NoSuchElementException("Paciente con DNI: " +
                        dni + " no fue encontrado"));
        try {
            patientUpdated.setActive(newState);
        } catch (NullPointerException e) {
            throw new NullPointerException("El valor ingresado no puede ser nulo " + e.getMessage());
        }
        patientRepository.save(patientUpdated);
    }

    @Override
    public List<Patient> findAllByActiveStatus(Boolean activeStatus) {
        return patientRepository.findAllByActive(activeStatus);
    }



}


