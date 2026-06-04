package com.prueba.consultorioMedico.controller;

import java.util.List;

import com.prueba.consultorioMedico.dto.*;
import com.prueba.consultorioMedico.service.medicalAppointment.MedicalAppointmentService;
import com.prueba.consultorioMedico.service.medicalRecord.MedicalRecordService;
import com.prueba.consultorioMedico.service.patient.PatientService;
import com.prueba.consultorioMedico.service.professional.ProfessionalService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.prueba.consultorioMedico.model.Patient;


@Slf4j
@RestController
@RequestMapping("/api/secretary")
@PreAuthorize("hasAnyRole('ADMIN','SECRETARY')")
@CrossOrigin("*")
public class SecretaryController {

    @Autowired
    private PatientService patientService;
    @Autowired
    private MedicalAppointmentService medicalAppointmentService;
    @Autowired
    private MedicalRecordService medicalRecordService;
    @Autowired
    private ProfessionalService professionalService;

    @GetMapping("/get")
    public ResponseEntity<List<Patient>> findAll() {
        return ResponseEntity.ok(patientService.findAll());
    }

    @GetMapping("/findPatientByDNI/{dni}")
    public ResponseEntity<?> findPatientByDNI(@PathVariable String dni) {
        try {
            if (dni.isEmpty() || !dni.matches("\\d{7,8}")) {
                return ResponseEntity.badRequest().body("El DNI debe ser un número de 7 u 8 dígitos.");
            }
            return ResponseEntity.ok().body(patientService.findByDNI(dni));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El valor ingresado es incorrecto");
        } finally {
            log.info("Busqueda de paciente con DNI: " + dni +" finalizada.");
        }
    }

    @GetMapping("/findPatientByLastname/{lastname}")
    public ResponseEntity<?> findPatientByLastname(@PathVariable String lastname) {
        return ResponseEntity.ok().body(patientService.findByLastName(lastname));
    }


    @GetMapping("/findAllByState/{accountState}")
    public ResponseEntity<?> findAllByState(@PathVariable boolean accountState) {
        return ResponseEntity.ok().body(patientService.findAllByActiveStatus(accountState));
    }

    @PostMapping("/addPatient")
    public ResponseEntity<Message> addPatient(@Valid @RequestBody Patient patient){
        patientService.add(patient);
        Message message = Message.builder().status(HttpStatus.OK).message("Paciente guardado correctamente").build();
        return ResponseEntity.ok(message);
    }

    @PutMapping("/updatePatient/{dni}")
    public ResponseEntity<Message> updatePatient(@PathVariable String dni, @RequestBody PatientDto patient) {
        patientService.update(patient, dni);
        Message message = Message.builder().status(HttpStatus.OK).message("Paciente actualizado correctamente").build();
        return ResponseEntity.ok(message);
    }

    @PatchMapping("/updatePatientState/{dni}/{state}")
    public ResponseEntity<Message> updatePatientState(@PathVariable String dni, @PathVariable Boolean state) {
        patientService.updateState(dni, state);
        Message message = Message.builder().status(HttpStatus.OK)
                .message("Estado de Paciente actualizado correctamente").build();
        return ResponseEntity.ok(message);
    }

    @PostMapping("/addMedicalAppointment")
    public ResponseEntity<Message> addMedicalAppointment(@RequestBody SimpleMedicalAppointmentDto sAppointmentDto) {
        medicalAppointmentService.add(sAppointmentDto);
        Message message = Message.builder().status(HttpStatus.OK).message("Cita medica generada correctamente").build();
        return ResponseEntity.ok(message);
    }

    @GetMapping("/findProfessional/{speciality}")
    public ResponseEntity<List<FullProfessionalDto>> findProfessionalBySpeciality(@PathVariable String speciality) {
        return ResponseEntity.ok().body(professionalService.findAllBySpeciality(speciality));
    }

    // Generar excepcion personalizada
    @GetMapping("/findMedicalRecord/{dni}")
    public ResponseEntity<?> findMedicalRecord(@PathVariable String dni) {
        try {
            if (dni.isEmpty() || !dni.matches("\\d{7,8}")) { 
                return ResponseEntity.badRequest().body("El DNI debe ser un número de 7 u 8 dígitos.");
            }
            return ResponseEntity.ok().body(medicalRecordService.findMedicalRecordByPatientDNI(dni));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El valor ingresado es incorrecto");
        } finally {
            log.info("Busqueda de Historial Medico de paciente con DNI: " + dni +" finalizada.");
        }
    }

    @GetMapping("/findMedicalAppointmentByPatient/{dni}")
    public ResponseEntity<?> findMedicalAppointmentByPatient(@PathVariable String dni) { //Se coloca ? ya que pueden ser varios tipos de retorno
        try {
            if (dni.isEmpty() || !dni.matches("\\d{7,8}")) { // Ajusta el patrón si es necesario
                return ResponseEntity.badRequest().body("El DNI debe ser un número de 7 u 8 dígitos.");
            }
            return ResponseEntity.ok().body(medicalAppointmentService.findAllByPatient(dni));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El valor ingresado es incorrecto");
        } finally {
            log.info("Busqueda de la cita del paciente con DNI: " + dni +" finalizada.");
        }
    }
    @GetMapping("/findMedicalAppointmentByPatient/{patientdni}/AndSpeciality/{specialityName}")
    public ResponseEntity<?> findMedicalAppointmentByPatient(@PathVariable String patientdni, @PathVariable String specialityName) {
        return ResponseEntity.ok().body(medicalAppointmentService.findAllByPatientAndSpecialty(patientdni, specialityName));
    }

    @PatchMapping("/updateMedicalAppointmentState/{id}/{state}")
    public ResponseEntity<Message> updateMedicalAppointmentState(@PathVariable Long id, @PathVariable String state) {
        medicalAppointmentService.updateAppointmentState(id, state);
        Message message = Message.builder().status(HttpStatus.OK).message("Cita medica actualizada correctamente")
                .build();
        return ResponseEntity.ok(message);
    }

}
