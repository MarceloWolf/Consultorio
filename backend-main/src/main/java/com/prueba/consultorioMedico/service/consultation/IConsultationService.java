package com.prueba.consultorioMedico.service.consultation;

import com.prueba.consultorioMedico.dto.ConsultationDto;

import java.util.List;


public interface IConsultationService {
    List<ConsultationDto> findAllConsultations();
    ConsultationDto findConsultationById(Long id);
    List<ConsultationDto> findConsultationsByMedicalRecordId(Long medicalRecordId);
    ConsultationDto addConsultation(String professionalDNIString, String patientDNI,String specialityName, ConsultationDto consultationDto);
    ConsultationDto updateConsultation(Long id, ConsultationDto consultationDto);
    void deleteConsultation(Long id);
    List<ConsultationDto> findAllOrderByDate();
    List<ConsultationDto>findConsultationsBySpecialityOrderByDate(String speciality);
}
