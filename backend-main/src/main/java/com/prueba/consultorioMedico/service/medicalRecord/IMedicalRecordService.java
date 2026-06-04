package com.prueba.consultorioMedico.service.medicalRecord;

import com.prueba.consultorioMedico.dto.MedicalRecordDto;
import com.prueba.consultorioMedico.model.MedicalRecord;

public interface IMedicalRecordService {
    void addMedicalRecord(MedicalRecord medicalRecord);
    MedicalRecord getMedicalRecordByPatient(String patientDni);
    void updateMedicalRecord(String dni,MedicalRecordDto medicalRecordDto);
    MedicalRecord findMedicalRecordByPatientDNI(String dni);
}
