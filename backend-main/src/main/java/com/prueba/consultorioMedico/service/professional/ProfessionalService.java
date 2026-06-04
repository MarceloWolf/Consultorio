package com.prueba.consultorioMedico.service.professional;


import com.prueba.consultorioMedico.dto.FullProfessionalDto;
import com.prueba.consultorioMedico.dto.SimpleProfessionalDto;
import com.prueba.consultorioMedico.enums.AccountStateEnum;
import com.prueba.consultorioMedico.model.*;
import com.prueba.consultorioMedico.repository.IMedicalAppointmentRepository;
import com.prueba.consultorioMedico.repository.IProfessionalRepository;
import com.prueba.consultorioMedico.service.businessDays.BusinessDaysService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class ProfessionalService implements IProfessionalService {
    private final IProfessionalRepository professionalRepository;
    private final IMedicalAppointmentRepository appointmentRepository;
    private final BusinessDaysService businessDaysService;

    @Override
    @Transactional(readOnly = true)
    public List<Professional> findAll() {
        return professionalRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SimpleProfessionalDto> findAllByAccountState(AccountStateEnum accountStateEnum) {
        List<Professional> professionalList = professionalRepository.findAllByAccountState(accountStateEnum);
        return toSimpleDtos(professionalList);
    }


    @Override
    @Transactional(readOnly = true)
    public List<FullProfessionalDto> findAllBySpeciality(String speciality) {
        List<Professional> professionalList =professionalRepository.findAllBySpecialityName(speciality);
        return toFullDtos(professionalList);
    }


    @Override
    @Transactional
    public void updateProfessional(SimpleProfessionalDto professionalDto, String dni) {
        Professional professional = professionalRepository.findByDNI(dni)
                .orElseThrow(() -> new NoSuchElementException("Profesional con DNI: " +
                        dni + " no fue encontrado"));

        professional.setName(professionalDto.getName());
        professional.setLastname(professionalDto.getLastname());
        professional.setDni(professionalDto.getDni());
        professional.setUsername(professionalDto.getUsername());
        professional.setPhoneNumber(professionalDto.getPhoneNumber());
        professional.setPhoneNumber(professionalDto.getPhoneNumber());
        professional.setAddress(professionalDto.getAddress());
        professional.setEmail(professionalDto.getEmail());
        professional.setAccountState(professionalDto.getAccountState());
        professionalRepository.save(professional);
    }


    @Override
    @Transactional(readOnly = true)
    public Professional findByDNI(String dni) {
        return professionalRepository.findByDNI(dni)
                .orElseThrow(() -> new NoSuchElementException("Professional with DNI: " +
                        dni + "has not been found"));
    }

    @Override
    public FullProfessionalDto findFullProfessionalByDNI(String dni) {
        Professional professional = professionalRepository.findByDNI(dni)
                .orElseThrow(() -> new NoSuchElementException("Professional with DNI: " +
                        dni + "has not been found"));
        return toFullDto(professional);
    }

    @Override
    @Transactional
    public void deleteProfessionalByDNI(String dni) {
        Professional professional = professionalRepository.findByDNI(dni)
                .orElseThrow(() ->new NoSuchElementException("Profesional con DNI: " +
                        dni + " no fue encontrado"));
        professional.setAccountState(AccountStateEnum.INACTIVE);
        professionalRepository.save(professional);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalAppointment> findMedicalAppointmentByProfessional(String profDni) {
        List<MedicalAppointment> lAppointments = appointmentRepository.findAllMedicalAppointmentByProfessional(profDni);
        if (lAppointments.isEmpty()) {
            return Collections.emptyList();
        }

        return lAppointments;
    }


    private List<SimpleProfessionalDto> toSimpleDtos(List<Professional> professionals) {
        return professionals.stream()
                .map(professional -> SimpleProfessionalDto.builder()
                        .dni(professional.getDni())
                        .address(professional.getAddress())
                        .username(professional.getUsername())
                        .name(professional.getName())
                        .lastname(professional.getLastname())
                        .email(professional.getEmail())
                        .phoneNumber(professional.getPhoneNumber())
                        .role(professional.getRole())
                        .accountState(professional.getAccountState())
                        .start(professional.getStart())
                        .end(professional.getEnd())
                        .build())
                .collect(Collectors.toList());
    }

    private List<FullProfessionalDto> toFullDtos(List<Professional> professionalList) {
        return professionalList.stream()
                .map(professional -> FullProfessionalDto.builder()
                        .dni(professional.getDni())
                        .name(professional.getName())
                        .dni(professional.getDni())
                        .lastname(professional.getLastname())
                        .start(professional.getStart())
                        .end(professional.getEnd())
                        .businessDays(professional.getBusinessDaysList() != null ?
                                professional.getBusinessDaysList().stream()
                                        .map(businessDaysService::toDto)
                                        .collect(Collectors.toSet()) : Collections.emptySet())
                        .build())
                .collect(Collectors.toList());
    }

    private FullProfessionalDto toFullDto(Professional professional) {
        return FullProfessionalDto.builder()
                        .dni(professional.getDni())
                        .name(professional.getName())
                        .dni(professional.getDni())
                        .lastname(professional.getLastname())
                        .start(professional.getStart())
                        .end(professional.getEnd())
                        .businessDays(professional.getBusinessDaysList() != null ?
                                professional.getBusinessDaysList().stream()
                                        .map(businessDaysService::toDto)
                                        .collect(Collectors.toSet()) : Collections.emptySet())
                        .build();

    }

}
