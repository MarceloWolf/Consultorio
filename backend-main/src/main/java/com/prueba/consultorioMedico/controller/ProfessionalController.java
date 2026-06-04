package com.prueba.consultorioMedico.controller;

import com.prueba.consultorioMedico.dto.ConsultationDto;
import com.prueba.consultorioMedico.dto.MedicalRecordDto;
import com.prueba.consultorioMedico.dto.Message;
import com.prueba.consultorioMedico.model.MedicalAppointment;
import com.prueba.consultorioMedico.model.MedicalRecord;
import com.prueba.consultorioMedico.model.Patient;
import com.prueba.consultorioMedico.model.Professional;
import com.prueba.consultorioMedico.service.consultation.ConsultationService;
import com.prueba.consultorioMedico.service.medicalRecord.MedicalRecordService;
import com.prueba.consultorioMedico.service.patient.PatientService;
import com.prueba.consultorioMedico.service.professional.IProfessionalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/professional")
// Recomendable poner la url del cliente frontend
@CrossOrigin("*")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','PROFESSIONAL')")
public class ProfessionalController {
    private final IProfessionalService professionalService;
    private final PatientService patientService;
    private final MedicalRecordService medicalRecordService;
    private final ConsultationService consultationService;

    @GetMapping("/getMedicalAppointments/{dni}")
    public ResponseEntity<List<MedicalAppointment>> findAllMedicalAppointments(@PathVariable String dni) {
        return ResponseEntity.ok(professionalService.findMedicalAppointmentByProfessional(dni));
    }

    @GetMapping("/getPatient/{dni}")
    public ResponseEntity<?> getPatient(@PathVariable String dni) {
        try {
            if (dni.isEmpty() || !dni.matches("\\d{7,8}")) { 
                return ResponseEntity.badRequest().body("El DNI debe ser un número de 7 u 8 dígitos.");
            }
            return ResponseEntity.ok().body(patientService.findByDNI(dni));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El valor ingresado es incorrecto");
        } finally {
            log.info("Busqueda de paciente con DNI: " + dni + " finalizada.");
        }

    }

    @GetMapping("/getMedicalRecord/{dni}")
    public ResponseEntity<?> findMedicalRecordByPatient(@PathVariable String dni) {
        try {
            if (dni.isEmpty() || !dni.matches("\\d{7,8}")) { 
                return ResponseEntity.badRequest().body("El DNI debe ser un número de 7 u 8 dígitos.");
            }
            return ResponseEntity.ok().body(medicalRecordService.findMedicalRecordByPatientDNI(dni));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El valor ingresado es incorrecto");
        } finally {
            log.info("Busqueda de Historial Medico de paciente con DNI: " + dni + " finalizada.");
        }
    }

    @PostMapping("/consultation/medical-record/{professionalDNi}/{patientDni}/{specialityName}")
    public ResponseEntity<Message> addConsultation(@PathVariable String professionalDNi,
            @PathVariable String patientDni,
            @PathVariable String specialityName,
            @RequestBody ConsultationDto consultationDto) {
        consultationService.addConsultation(professionalDNi, patientDni, specialityName, consultationDto);
        Message message = Message.builder().status(HttpStatus.OK).message("Consulta guardada correctamente").build();
        return ResponseEntity.ok(message);
    }

    @PutMapping("/updateMedicalRecord/{dni}")
    public ResponseEntity<Message> updateMedicalRecord(@PathVariable String dni,
            @RequestBody MedicalRecordDto medicalRecordDto) {
        medicalRecordService.updateMedicalRecord(dni, medicalRecordDto);
        Message message = Message.builder().status(HttpStatus.OK).message("Historial medico actualizado correctamente")
                .build();
        return ResponseEntity.ok(message);
    }

    @GetMapping("/get/{dni}")
    @PreAuthorize("hasRole('SECRETARY')")
    public ResponseEntity<?> getProfessional(@PathVariable String dni) {
        return ResponseEntity.ok(professionalService.findFullProfessionalByDNI(dni));
    }

}
