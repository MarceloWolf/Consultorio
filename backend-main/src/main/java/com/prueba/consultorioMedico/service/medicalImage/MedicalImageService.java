package com.prueba.consultorioMedico.service.medicalImage;

import com.prueba.consultorioMedico.model.AuditLog;
import com.prueba.consultorioMedico.model.MedicalImage;
import com.prueba.consultorioMedico.model.Patient;
import com.prueba.consultorioMedico.repository.AuditLogRepository;
import com.prueba.consultorioMedico.repository.IMedicalImageRepository;
import com.prueba.consultorioMedico.repository.IPatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MedicalImageService implements IMedicalImageService {

    private final IMedicalImageRepository medicalImageRepository;
    private final IPatientRepository patientRepository;
    private final AuditLogRepository auditLogRepository;

    @Override
    @Transactional
    public List<MedicalImage> getMedicalImagesByPatient(String patientDni) {
        List<MedicalImage> images = medicalImageRepository.findByPatientDni(patientDni);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        auditLogRepository.save(AuditLog.builder()
                .username(username)
                .patientDni(patientDni)
                .action("VIEW_MEDICAL_IMAGES")
                .timestamp(LocalDateTime.now())
                .details("Accessed list of medical images for patient DNI: " + patientDni)
                .build());

        return images;
    }

    @Override
    @Transactional
    public List<MedicalImage> getMedicalImagesByPatientAndType(String patientDni, String fileType) {
        List<MedicalImage> images = medicalImageRepository.findByPatientDniAndFileType(patientDni, fileType);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        auditLogRepository.save(AuditLog.builder()
                .username(username)
                .patientDni(patientDni)
                .action("VIEW_MEDICAL_IMAGES_BY_TYPE")
                .timestamp(LocalDateTime.now())
                .details("Accessed " + fileType + " images for patient DNI: " + patientDni)
                .build());

        return images;
    }

    @Override
    @Transactional
    public MedicalImage saveMedicalImage(String patientDni, String fileName, String fileType, String comments, String imageData) {
        Patient patient = patientRepository.findByDNI(patientDni)
                .orElseThrow(() -> new NoSuchElementException("Patient with DNI: " + patientDni + " not found"));

        MedicalImage medicalImage = MedicalImage.builder()
                .patient(patient)
                .fileName(fileName)
                .fileType(fileType)
                .uploadDate(LocalDate.now())
                .comments(comments)
                .imageData(imageData)
                .build();

        MedicalImage saved = medicalImageRepository.save(medicalImage);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        auditLogRepository.save(AuditLog.builder()
                .username(username)
                .patientDni(patientDni)
                .action("UPLOAD_MEDICAL_IMAGE")
                .timestamp(LocalDateTime.now())
                .details("Uploaded medical image " + fileName + " (" + fileType + ") for patient DNI: " + patientDni)
                .build());

        return saved;
    }

    @Override
    @Transactional
    public void deleteMedicalImage(Long imageId) {
        MedicalImage medicalImage = medicalImageRepository.findById(imageId)
                .orElseThrow(() -> new NoSuchElementException("Medical Image with ID " + imageId + " not found"));

        String patientDni = medicalImage.getPatient().getDni();
        String fileName = medicalImage.getFileName();
        String fileType = medicalImage.getFileType();

        medicalImageRepository.delete(medicalImage);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        auditLogRepository.save(AuditLog.builder()
                .username(username)
                .patientDni(patientDni)
                .action("DELETE_MEDICAL_IMAGE")
                .timestamp(LocalDateTime.now())
                .details("Deleted medical image " + fileName + " (" + fileType + ") with ID " + imageId + " for patient DNI: " + patientDni)
                .build());
    }
}
