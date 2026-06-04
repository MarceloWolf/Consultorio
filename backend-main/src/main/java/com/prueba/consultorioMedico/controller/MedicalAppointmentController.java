package com.prueba.consultorioMedico.controller;

import com.prueba.consultorioMedico.dto.AppointmentFilterDto;
import com.prueba.consultorioMedico.dto.FullMedicalAppointmentDto;
import com.prueba.consultorioMedico.dto.MedicalAppointmentDataAllowedToUpdateDto;
import com.prueba.consultorioMedico.dto.Message;
import com.prueba.consultorioMedico.dto.SimpleMedicalAppointmentDto;
import com.prueba.consultorioMedico.enums.MedicalAppointmentStateEnum;
import com.prueba.consultorioMedico.model.MedicalAppointment;
import com.prueba.consultorioMedico.service.medicalAppointment.IMedicalAppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointment")
// Recomendable poner la url del cliente frontend
@CrossOrigin("*")
@PreAuthorize("hasAnyRole('ADMIN','SECRETARY')")
@RequiredArgsConstructor
public class MedicalAppointmentController {
    private final IMedicalAppointmentService medicalAppointmentService;

    @GetMapping("/get")
    public ResponseEntity<List<FullMedicalAppointmentDto>> findAll() {
        return ResponseEntity.ok(medicalAppointmentService.findAll());
    }

    /*
     * Este metodo lo voy a usar para cuando el front vaya a la pagina de
     * actualizacion
     * Poder obtener los datos y autocompletar el formulario, por eso es que retorno
     * el dto simple.
     */
    @GetMapping("/get/{appointmentId}")
    public ResponseEntity<SimpleMedicalAppointmentDto> findById(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(medicalAppointmentService.findById(appointmentId));
    }

    @GetMapping("/get/patient/{patientDni}")
    public ResponseEntity<List<FullMedicalAppointmentDto>> findByPatient(@PathVariable String patientDni) {
        return ResponseEntity.ok(medicalAppointmentService.findAllByPatient(patientDni));
    }

    @GetMapping("/get/professional/{professionalDni}")
    public ResponseEntity<List<FullMedicalAppointmentDto>> findByProfessional(@PathVariable String professionalDni) {
        return ResponseEntity.ok(medicalAppointmentService.findAllByProfessional(professionalDni));
    }

    @GetMapping("/get/speciality/{specialityName}")
    public ResponseEntity<List<FullMedicalAppointmentDto>> findBySpeciality(@PathVariable String specialityName) {
        return ResponseEntity.ok(medicalAppointmentService.findAllBySpeciality(specialityName));
    }

    @GetMapping("/getByState/{medicalAppState}")
    public ResponseEntity<List<FullMedicalAppointmentDto>> findByState(
            @PathVariable MedicalAppointmentStateEnum medicalAppState) {
        return ResponseEntity.ok().body(medicalAppointmentService.getMedicalAppointmentsByState(medicalAppState));
    }

    @GetMapping("/findAppointmentByFilters")
    public ResponseEntity<List<FullMedicalAppointmentDto>> findByFilters(
            @RequestParam(required = false) String specialityName,
            @RequestParam(required = false) String professionalDni,
            @RequestParam(required = false) LocalDate selectedDate) {
                
                if("undefined".equals(professionalDni))
                {
                   professionalDni = null;
                }
        
        System.out.println(specialityName + " " + professionalDni + " " + selectedDate);
        return ResponseEntity.ok().body(medicalAppointmentService.findAllByFilters(specialityName, professionalDni, selectedDate));
    }

    @GetMapping("/getByDate/{date}")
    public ResponseEntity<List<FullMedicalAppointmentDto>> findByDate(@PathVariable LocalDate date){
        return  ResponseEntity.ok().body(medicalAppointmentService.findAllByDate(date));
    }

    @PostMapping("/add")
    public ResponseEntity<Message> addAppointment(
            @RequestBody SimpleMedicalAppointmentDto simpleMedicalAppointmentDto) {
        medicalAppointmentService.add(simpleMedicalAppointmentDto);
        Message message = Message.builder().status(HttpStatus.OK).message("Cita guardada correctamente").build();
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/delete/{medicalAppointmentId}")
    public ResponseEntity<Message> deleteAppointment(@PathVariable Long medicalAppointmentId) {
        medicalAppointmentService.deleteAppointment(medicalAppointmentId);
        Message message = Message.builder().status(HttpStatus.OK).message("Cita eliminada correctamente").build();
        return ResponseEntity.ok(message);
    }

    @PutMapping("/rescheduleMedicalAppointment/{id}")
    public ResponseEntity<Message> updateMedicalAppointmentState(@PathVariable Long id,
            @RequestBody MedicalAppointmentDataAllowedToUpdateDto mAllowedToUpdateDto) {
        medicalAppointmentService.reschedule(id, mAllowedToUpdateDto);
        Message message = Message.builder().status(HttpStatus.OK).message("Cita medica actualizada correctamente")
                .build();
        return ResponseEntity.ok(message);
    }

    @PatchMapping("/cancelAppointment/{id}")
    public ResponseEntity<Message> cancelAppointment(@PathVariable Long id) {
        medicalAppointmentService.cancel(id);
        Message message = Message.builder().status(HttpStatus.OK).message("Cita cancelada correctamente")
                .build();
        return ResponseEntity.ok(message);
    }

}
