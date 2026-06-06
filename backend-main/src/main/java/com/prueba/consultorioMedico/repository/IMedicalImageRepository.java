package com.prueba.consultorioMedico.repository;

import com.prueba.consultorioMedico.model.MedicalImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMedicalImageRepository extends JpaRepository<MedicalImage, Long> {
    List<MedicalImage> findByPatientDni(String patientDni);
    List<MedicalImage> findByPatientDniAndFileType(String patientDni, String fileType);
}
