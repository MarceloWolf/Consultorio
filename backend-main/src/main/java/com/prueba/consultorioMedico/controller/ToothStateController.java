package com.prueba.consultorioMedico.controller;

import com.prueba.consultorioMedico.dto.Message;
import com.prueba.consultorioMedico.dto.ToothStateDto;
import com.prueba.consultorioMedico.model.ToothState;
import com.prueba.consultorioMedico.service.toothState.IToothStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tooth-states")
@RequiredArgsConstructor
@CrossOrigin("*")
@PreAuthorize("hasAnyRole('ADMIN','PROFESSIONAL')")
public class ToothStateController {

    private final IToothStateService toothStateService;

    @GetMapping("/{patientDni}")
    public ResponseEntity<List<ToothState>> getToothStates(@PathVariable String patientDni) {
        List<ToothState> states = toothStateService.getToothStatesByPatient(patientDni);
        return ResponseEntity.ok(states);
    }

    @PostMapping("/{patientDni}")
    public ResponseEntity<ToothState> saveOrUpdateToothState(
            @PathVariable String patientDni,
            @RequestBody ToothStateDto dto) {
        ToothState saved = toothStateService.saveOrUpdateToothState(
                patientDni,
                dto.getToothNumber(),
                dto.getState(),
                dto.getNotes()
        );
        return ResponseEntity.ok(saved);
    }
}
