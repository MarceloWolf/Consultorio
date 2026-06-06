package com.prueba.consultorioMedico.config;

import com.prueba.consultorioMedico.model.MedicalImage;
import com.prueba.consultorioMedico.model.Patient;
import com.prueba.consultorioMedico.model.ToothState;
import com.prueba.consultorioMedico.repository.IMedicalImageRepository;
import com.prueba.consultorioMedico.repository.IPatientRepository;
import com.prueba.consultorioMedico.repository.IToothStateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final IToothStateRepository toothStateRepository;
    private final IMedicalImageRepository medicalImageRepository;
    private final IPatientRepository patientRepository;

    // A valid Base64 image showing a teeth outline or simulated x-ray
    private static final String SAMPLE_RX_BASE64 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAQAAAAEAAQMAAABFGLnPAAAABlBMVEUAAAD///+l2Z/dAAAACXBIWXMAAA7EAAAOxAGVKw4bAAAARklEQVRo3u3PQREAAAgDMObfNCrsYQcHOFiSpKqrq6urq6urq6urq6urq6urq6urq6urq6urq6urq6urq6urq6urq6urq2tXFmICW4/pU+sAAAAASUVORK5CYII=";
    private static final String SAMPLE_TAC_BASE64 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAQAAAAEAAQMAAABFGLnPAAAABlBMVEUAAAD///+l2Z/dAAAACXBIWXMAAA7EAAAOxAGVKw4bAAAARklEQVRo3u3PQREAAAgDMObfNCrsYQcHOFiSpKqrq6urq6urq6urq6urq6urq6urq6urq6urq6urq6urq6urq6urq6urq2tXFmICW4/pU+sAAAAASUVORK5CYII=";

    @Override
    public void run(String... args) throws Exception {
        log.info("Checking if sample clinical data needs to be initialized...");
        
        List<Patient> patients = patientRepository.findAll();
        if (patients.isEmpty()) {
            log.info("No patients found in the database. Skipping sample clinical data initialization.");
            return;
        }

        // Initialize sample Tooth States if empty
        if (toothStateRepository.count() == 0) {
            log.info("Initializing sample tooth states (odontogram)...");
            for (Patient patient : patients) {
                // Let's create some sample tooth states for each patient to display in the odontogram
                toothStateRepository.save(ToothState.builder()
                        .patient(patient)
                        .toothNumber(18)
                        .state("CARIES")
                        .notes("Caries oclusal profunda detectada en examen visual.")
                        .build());
                toothStateRepository.save(ToothState.builder()
                        .patient(patient)
                        .toothNumber(21)
                        .state("CORONA")
                        .notes("Corona metal-cerámica colocada anteriormente en buen estado.")
                        .build());
                toothStateRepository.save(ToothState.builder()
                        .patient(patient)
                        .toothNumber(36)
                        .state("TRATAMIENTO_CONDUCTO")
                        .notes("Tratamiento de conducto (endodoncia) terminado. Pendiente perno.")
                        .build());
                toothStateRepository.save(ToothState.builder()
                        .patient(patient)
                        .toothNumber(48)
                        .state("AUSENTE")
                        .notes("Tercer molar inferior derecho extraído anteriormente.")
                        .build());
            }
            log.info("Tooth states initialized successfully.");
        }

        // Initialize sample Medical Images if empty
        if (medicalImageRepository.count() == 0) {
            log.info("Initializing sample medical images (RX / TAC)...");
            for (Patient patient : patients) {
                medicalImageRepository.save(MedicalImage.builder()
                        .patient(patient)
                        .fileName("Radiografia_Panoramica_Control.png")
                        .fileType("RX")
                        .uploadDate(LocalDate.now().minusMonths(3))
                        .comments("Placa de control panorámica. Se aprecian conductos obturados en pieza 36 y caries en 18.")
                        .imageData(SAMPLE_RX_BASE64)
                        .build());

                medicalImageRepository.save(MedicalImage.builder()
                        .patient(patient)
                        .fileName("Tomografia_ConeBeam_Mandibular.png")
                        .fileType("TAC")
                        .uploadDate(LocalDate.now().minusMonths(1))
                        .comments("Tomografía computada de haz cónico (CBCT) del sector inferior para planeamiento de implante en zona de pieza 48.")
                        .imageData(SAMPLE_TAC_BASE64)
                        .build());
            }
            log.info("Medical images initialized successfully.");
        }
    }
}
