package com.prueba.consultorioMedico.service.toothState;

import com.prueba.consultorioMedico.model.AuditLog;
import com.prueba.consultorioMedico.model.Patient;
import com.prueba.consultorioMedico.model.ToothState;
import com.prueba.consultorioMedico.repository.AuditLogRepository;
import com.prueba.consultorioMedico.repository.IPatientRepository;
import com.prueba.consultorioMedico.repository.IToothStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ToothStateService implements IToothStateService {

    private final IToothStateRepository toothStateRepository;
    private final IPatientRepository patientRepository;
    private final AuditLogRepository auditLogRepository;

    @Override
    @Transactional
    public List<ToothState> getToothStatesByPatient(String patientDni) {
        List<ToothState> states = toothStateRepository.findByPatientDni(patientDni);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        auditLogRepository.save(AuditLog.builder()
                .username(username)
                .patientDni(patientDni)
                .action("VIEW_ODONTOGRAM")
                .timestamp(LocalDateTime.now())
                .details("Accessed odontogram tooth states of patient DNI: " + patientDni)
                .build());

        return states;
    }

    @Override
    @Transactional
    public ToothState saveOrUpdateToothState(String patientDni, Integer toothNumber, String state, String notes) {
        Patient patient = patientRepository.findByDNI(patientDni)
                .orElseThrow(() -> new NoSuchElementException("Patient with DNI: " + patientDni + " not found"));

        Optional<ToothState> existingTooth = toothStateRepository.findByPatientDniAndToothNumber(patientDni, toothNumber);
        ToothState toothState;

        if (existingTooth.isPresent()) {
            toothState = existingTooth.get();
            toothState.setState(state);
            toothState.setNotes(notes);
        } else {
            toothState = ToothState.builder()
                    .patient(patient)
                    .toothNumber(toothNumber)
                    .state(state)
                    .notes(notes)
                    .build();
        }

        ToothState saved = toothStateRepository.save(toothState);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        auditLogRepository.save(AuditLog.builder()
                .username(username)
                .patientDni(patientDni)
                .action("UPDATE_ODONTOGRAM")
                .timestamp(LocalDateTime.now())
                .details("Updated tooth " + toothNumber + " state to " + state + " for patient DNI: " + patientDni)
                .build());

        return saved;
    }
}
