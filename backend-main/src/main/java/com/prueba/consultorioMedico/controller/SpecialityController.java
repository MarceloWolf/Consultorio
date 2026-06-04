package com.prueba.consultorioMedico.controller;

import com.prueba.consultorioMedico.dto.Message;
import com.prueba.consultorioMedico.dto.SpecialityDto;
import com.prueba.consultorioMedico.model.Speciality;
import com.prueba.consultorioMedico.service.speciality.ISpecialityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/speciality")
//Recomendable poner la url del cliente frontend
@CrossOrigin("*")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class SpecialityController {
    @Autowired
    private final ISpecialityService specialityService;

    @GetMapping("/get")
    public ResponseEntity<List<Speciality>> findAll(){
        return ResponseEntity.ok(specialityService.findAll());
    }

    @GetMapping("/get/professional/{professionalDni}")
    public ResponseEntity<List<Speciality>> findAllByProfessional(@PathVariable String professionalDni){
        return ResponseEntity.ok(specialityService.findSpecialitiesByProfessional(professionalDni));
    }

    @PostMapping("/add")
    public ResponseEntity<Message> addProfessional(@RequestBody SpecialityDto specialityDto){
        Speciality speciality = Speciality.builder().name(specialityDto.getName()).build();
        specialityService.add(speciality);
        Message message = Message.builder().status(HttpStatus.OK).message("Especialidad guardada correctamente").build();
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/deleteSpeciality/{specialityName}")
    public ResponseEntity<Message> deleteSpeciality(@PathVariable String specialityName ){
        specialityService.deleteSpeciality(specialityName);
        Message message = Message.builder().status(HttpStatus.OK).message("Especialidad eliminada correctamente").build();
        return ResponseEntity.ok(message);
    }
}
