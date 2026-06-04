package com.prueba.consultorioMedico.service.medicalRecord;

import com.prueba.consultorioMedico.dto.MedicalRecordDto;
import com.prueba.consultorioMedico.model.MedicalRecord;
import com.prueba.consultorioMedico.repository.IMedicalRecordRepository;
import lombok.RequiredArgsConstructor;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MedicalRecordService implements IMedicalRecordService {
    private final IMedicalRecordRepository imedicalRecordRepository;

    @Override
    @Transactional
    public void addMedicalRecord(MedicalRecord medicalRecord) {
        imedicalRecordRepository.save(medicalRecord);
    }

    @Override
    @Transactional(readOnly = true)
    public MedicalRecord getMedicalRecordByPatient(String patientDni) {
        return imedicalRecordRepository.findByPatientDni(patientDni).orElseThrow(() -> new NoSuchElementException(
                "Medical Record of patient with DNI: " + patientDni + " has not been found"));
    }

    @Override
    @Transactional
    public void updateMedicalRecord(String dni, MedicalRecordDto medicalRecordDto) {
        MedicalRecord medicalRecord = this.getMedicalRecordByPatient(dni);

        medicalRecord.setDescription(medicalRecordDto.getDescription());
        medicalRecord.setHeight(medicalRecordDto.getHeight());
        medicalRecord.setWeight(medicalRecordDto.getWeight());
        medicalRecord.setBloodGroup(medicalRecordDto.getBloodGroup());

        this.addMedicalRecord(medicalRecord);
    }

    @Override
    @Transactional(readOnly = true)
    public MedicalRecord findMedicalRecordByPatientDNI(String dni) {
        return imedicalRecordRepository.findByPatientDni(dni)
                .orElseThrow((() -> new NoSuchElementException("Medical Record with Patient with DNI: " +
                        dni + "has not been found")));
    }
}
