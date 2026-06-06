package com.prueba.consultorioMedico.service.medicalRecord;

import com.prueba.consultorioMedico.dto.MedicalRecordDto;
import com.prueba.consultorioMedico.model.AuditLog;
import com.prueba.consultorioMedico.model.MedicalRecord;
import com.prueba.consultorioMedico.repository.AuditLogRepository;
import com.prueba.consultorioMedico.repository.IMedicalRecordRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MedicalRecordService implements IMedicalRecordService {
    private final IMedicalRecordRepository imedicalRecordRepository;
    private final AuditLogRepository auditLogRepository;

    @Override
    @Transactional
    public void addMedicalRecord(MedicalRecord medicalRecord) {
        imedicalRecordRepository.save(medicalRecord);
    }

    @Override
    @Transactional
    public MedicalRecord getMedicalRecordByPatient(String patientDni) {
        MedicalRecord record = imedicalRecordRepository.findByPatientDni(patientDni).orElseThrow(() -> new NoSuchElementException(
                "Medical Record of patient with DNI: " + patientDni + " has not been found"));
        
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        auditLogRepository.save(AuditLog.builder()
                .username(username)
                .patientDni(patientDni)
                .action("VIEW_MEDICAL_RECORD")
                .timestamp(LocalDateTime.now())
                .details("Accessed medical record of patient DNI: " + patientDni)
                .build());

        return record;
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

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        auditLogRepository.save(AuditLog.builder()
                .username(username)
                .patientDni(dni)
                .action("UPDATE_MEDICAL_RECORD")
                .timestamp(LocalDateTime.now())
                .details("Updated medical record for patient DNI: " + dni)
                .build());
    }

    @Override
    @Transactional
    public MedicalRecord findMedicalRecordByPatientDNI(String dni) {
        MedicalRecord record = imedicalRecordRepository.findByPatientDni(dni)
                .orElseThrow((() -> new NoSuchElementException("Medical Record with Patient with DNI: " +
                        dni + "has not been found")));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        auditLogRepository.save(AuditLog.builder()
                .username(username)
                .patientDni(dni)
                .action("VIEW_MEDICAL_RECORD")
                .timestamp(LocalDateTime.now())
                .details("Accessed medical record by patient DNI: " + dni)
                .build());

        return record;
    }
}
