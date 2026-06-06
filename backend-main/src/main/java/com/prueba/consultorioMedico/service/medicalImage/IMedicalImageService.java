package com.prueba.consultorioMedico.service.medicalImage;

import com.prueba.consultorioMedico.model.MedicalImage;

import java.util.List;

public interface IMedicalImageService {
    List<MedicalImage> getMedicalImagesByPatient(String patientDni);
    List<MedicalImage> getMedicalImagesByPatientAndType(String patientDni, String fileType);
    MedicalImage saveMedicalImage(String patientDni, String fileName, String fileType, String comments, String imageData);
    void deleteMedicalImage(Long imageId);
}
