package com.prueba.consultorioMedico.controller;

import com.prueba.consultorioMedico.dto.ConsultationDto;
import com.prueba.consultorioMedico.service.consultation.IConsultationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultations")
@CrossOrigin("*")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','PROFESSIONAL')")
public class ConsultationController {

    private final IConsultationService consultationService;

    @GetMapping
    public ResponseEntity<List<ConsultationDto>> getAllConsultations() {
        return ResponseEntity.ok(consultationService.findAllConsultations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultationDto> getConsultationById(@PathVariable Long id) {
        return ResponseEntity.ok(consultationService.findConsultationById(id));
    }

    @GetMapping("/medical-record/{id}")
    public ResponseEntity<List<ConsultationDto>> getConsultationsByMedicalRecordId(@PathVariable Long id) {
        return ResponseEntity.ok(consultationService.findConsultationsByMedicalRecordId(id));
    }

    @GetMapping("/findAllOrderByDate")
    public ResponseEntity<List<ConsultationDto>> findAllOrderByDate() {
        return ResponseEntity.ok(consultationService.findAllOrderByDate());
    }

    @GetMapping("/findAllBySpeciality/{name}")//Hacer validacion de que sea una especialidad existente
    public ResponseEntity<List<ConsultationDto>> findAllBySpeciality(@PathVariable String name) {
        return ResponseEntity.ok(consultationService.findConsultationsBySpecialityOrderByDate(name));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultationDto> updateConsultation(@PathVariable Long id, @RequestBody ConsultationDto consultationDto) {
        return ResponseEntity.ok(consultationService.updateConsultation(id, consultationDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsultation(@PathVariable Long id) {
        consultationService.deleteConsultation(id);
        return ResponseEntity.noContent().build();
    }
}
