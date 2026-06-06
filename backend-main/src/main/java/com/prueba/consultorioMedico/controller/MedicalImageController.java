package com.prueba.consultorioMedico.controller;

import com.prueba.consultorioMedico.dto.MedicalImageDto;
import com.prueba.consultorioMedico.dto.Message;
import com.prueba.consultorioMedico.model.MedicalImage;
import com.prueba.consultorioMedico.service.medicalImage.IMedicalImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-images")
@RequiredArgsConstructor
@CrossOrigin("*")
@PreAuthorize("hasAnyRole('ADMIN','PROFESSIONAL')")
public class MedicalImageController {

    private final IMedicalImageService medicalImageService;

    @GetMapping("/{patientDni}")
    public ResponseEntity<List<MedicalImage>> getMedicalImages(@PathVariable String patientDni) {
        List<MedicalImage> images = medicalImageService.getMedicalImagesByPatient(patientDni);
        return ResponseEntity.ok(images);
    }

    @GetMapping("/{patientDni}/type/{fileType}")
    public ResponseEntity<List<MedicalImage>> getMedicalImagesByType(
            @PathVariable String patientDni,
            @PathVariable String fileType) {
        List<MedicalImage> images = medicalImageService.getMedicalImagesByPatientAndType(patientDni, fileType);
        return ResponseEntity.ok(images);
    }

    @PostMapping("/{patientDni}")
    public ResponseEntity<MedicalImage> uploadMedicalImage(
            @PathVariable String patientDni,
            @RequestBody MedicalImageDto dto) {
        MedicalImage saved = medicalImageService.saveMedicalImage(
                patientDni,
                dto.getFileName(),
                dto.getFileType(),
                dto.getComments(),
                dto.getImageData()
        );
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<Message> deleteMedicalImage(@PathVariable Long imageId) {
        medicalImageService.deleteMedicalImage(imageId);
        Message message = Message.builder()
                .status(HttpStatus.OK)
                .message("Imagen medica eliminada exitosamente.")
                .build();
        return ResponseEntity.ok(message);
    }
}
