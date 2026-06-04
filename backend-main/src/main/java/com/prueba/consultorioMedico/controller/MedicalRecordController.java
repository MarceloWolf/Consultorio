package com.prueba.consultorioMedico.controller;

import com.prueba.consultorioMedico.dto.MedicalRecordDto;
import com.prueba.consultorioMedico.dto.Message;
import com.prueba.consultorioMedico.model.MedicalRecord;
import com.prueba.consultorioMedico.service.medicalRecord.IMedicalRecordService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
@CrossOrigin("*")
@PreAuthorize("hasAnyRole('ADMIN','PROFESSIONAL')")
public class MedicalRecordController {

    private final IMedicalRecordService medicalRecordService;

    // Agregar una nueva historia clínica
    @PostMapping
    public void addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        medicalRecordService.addMedicalRecord(medicalRecord);
    }

    // Obtener la historia clínica de un paciente por su DNI
    @GetMapping("/{patientDni}")
    public MedicalRecord getMedicalRecordByPatient(@PathVariable String patientDni) {
        return medicalRecordService.getMedicalRecordByPatient(patientDni);
    }

    @PutMapping("/updateMedicalRecord/{patientDni}")
    public ResponseEntity<Message> updateMedicalRecord(@PathVariable String patientDni, @RequestBody MedicalRecordDto medicalRecord) {
        medicalRecordService.updateMedicalRecord(patientDni, medicalRecord);
        Message message = Message.builder().status(HttpStatus.OK).message("Medical record actualizado exitosamente.")
                .build();
        return ResponseEntity.ok().body(message);
    }
}
