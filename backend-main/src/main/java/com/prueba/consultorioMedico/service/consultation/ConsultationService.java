package com.prueba.consultorioMedico.service.consultation;

import com.prueba.consultorioMedico.dto.ConsultationDto;
import com.prueba.consultorioMedico.exception.PatientAlreadyHasAppointmentException;
import com.prueba.consultorioMedico.model.AuditLog;
import com.prueba.consultorioMedico.model.Consultation;
import com.prueba.consultorioMedico.model.Patient;
import com.prueba.consultorioMedico.model.Professional;
import com.prueba.consultorioMedico.repository.AuditLogRepository;
import com.prueba.consultorioMedico.repository.IConsultationRepository;
import com.prueba.consultorioMedico.service.patient.PatientService;
import com.prueba.consultorioMedico.service.professional.IProfessionalService;
import com.prueba.consultorioMedico.service.speciality.ISpecialityService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultationService implements IConsultationService {
    private final IConsultationRepository consultationRepository;
    private final IProfessionalService professionalService;
    private final ISpecialityService specialityService;
    private final PatientService patientService;
    private final AuditLogRepository auditLogRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ConsultationDto> findAllConsultations() {
        return consultationRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ConsultationDto findConsultationById(Long id) {
        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Consultation not found"));
        return toDto(consultation);
    }

    @Override
    @Transactional
    public List<ConsultationDto> findConsultationsByMedicalRecordId(Long medicalRecordId) {
        List<Consultation> consultations = consultationRepository.findByMedicalRecordId(medicalRecordId);
        
        if (!consultations.isEmpty()) {
            String patientDni = consultations.get(0).getMedicalRecord().getPatient().getDni();
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            auditLogRepository.save(AuditLog.builder()
                    .username(username)
                    .patientDni(patientDni)
                    .action("VIEW_CONSULTATIONS")
                    .timestamp(LocalDateTime.now())
                    .details("Viewed consultations list for patient DNI: " + patientDni)
                    .build());
        }

        return consultations.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsultationDto> findAllOrderByDate() {
        return consultationRepository.findAllOrderByDate()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsultationDto> findConsultationsBySpecialityOrderByDate(String speciality) {
        if(specialityService.findByName(speciality) != null)
        {
            return consultationRepository.findConsultationsBySpecialityOrderByDate(speciality)
            .stream()
            .map(this::toDto)
            .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    @Transactional
    public ConsultationDto addConsultation(String professionalDNI, String patientDNI,String specialityName, ConsultationDto consultationDto) {
        Consultation consultation = toEntity(consultationDto);
        // Validacion de existencia
        Professional professional = professionalService.findByDNI(professionalDNI);
        Patient patient = patientService.findByDNI(patientDNI);
        if(professional.getSpecialityList().stream().anyMatch(speciality -> speciality.getSpeciality().getName().equals(specialityName)))
        {
            consultation.setSpecialityName(specialityName); //Agregar al endpoint el nombre de la especialidad
            consultation.setProfessional(professional);
            consultation.setMedicalRecord(patient.getMedicalRecord());
            Consultation savedConsultation = consultationRepository.save(consultation);

            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            auditLogRepository.save(AuditLog.builder()
                    .username(username)
                    .patientDni(patientDNI)
                    .action("CREATE_CONSULTATION")
                    .timestamp(LocalDateTime.now())
                    .details("Created consultation with professional: " + professional.getName() + " " + professional.getLastname())
                    .build());

            return toDto(savedConsultation);
        
        }else{
            throw new NoSuchElementException("La especialidad ingresada no coincide con alguna de las especialidades del profesional");
        }
    }

    @Override
    @Transactional
    public ConsultationDto updateConsultation(Long id, ConsultationDto consultationDto) {
        Consultation existingConsultation = consultationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Consultation not found"));

        existingConsultation.setReason(consultationDto.getReason());
        existingConsultation.setDiagnosis(consultationDto.getDiagnosis());
        existingConsultation.setTreatment(consultationDto.getTreatment());

        Consultation updatedConsultation = consultationRepository.save(existingConsultation);

        String patientDni = existingConsultation.getMedicalRecord().getPatient().getDni();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        auditLogRepository.save(AuditLog.builder()
                .username(username)
                .patientDni(patientDni)
                .action("UPDATE_CONSULTATION")
                .timestamp(LocalDateTime.now())
                .details("Updated consultation details")
                .build());

        return toDto(updatedConsultation);
    }

    @Override
    @Transactional
    public void deleteConsultation(Long id) {
        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Consultation not found"));
        
        String patientDni = consultation.getMedicalRecord().getPatient().getDni();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        
        consultationRepository.delete(consultation);

        auditLogRepository.save(AuditLog.builder()
                .username(username)
                .patientDni(patientDni)
                .action("DELETE_CONSULTATION")
                .timestamp(LocalDateTime.now())
                .details("Deleted consultation ID: " + id)
                .build());
    }

    private ConsultationDto toDto(Consultation consultation) {
        return ConsultationDto.builder()
                .date(consultation.getDate())
                .time(consultation.getTime())
                .specialityName(consultation.getSpecialityName())
                .reason(consultation.getReason())
                .diagnosis(consultation.getDiagnosis())
                .treatment(consultation.getTreatment())
                .build();
    }

    private Consultation toEntity(ConsultationDto consultationDto) {
        return Consultation.builder()
                .date(LocalDate.now())
                .time(LocalTime.now())
                .reason(consultationDto.getReason())
                .diagnosis(consultationDto.getDiagnosis())
                .treatment(consultationDto.getTreatment())
                .build();
    }
}
