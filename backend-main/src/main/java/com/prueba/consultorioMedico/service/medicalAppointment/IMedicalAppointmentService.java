package com.prueba.consultorioMedico.service.medicalAppointment;

import com.prueba.consultorioMedico.dto.AppointmentFilterDto;
import com.prueba.consultorioMedico.dto.FullMedicalAppointmentDto;
import com.prueba.consultorioMedico.dto.MedicalAppointmentDataAllowedToUpdateDto;
import com.prueba.consultorioMedico.dto.SimpleMedicalAppointmentDto;
import com.prueba.consultorioMedico.enums.MedicalAppointmentStateEnum;
import com.prueba.consultorioMedico.model.MedicalAppointment;
import com.prueba.consultorioMedico.service.IGenericService;

import java.time.LocalDate;
import java.util.List;

public interface IMedicalAppointmentService extends IGenericService<FullMedicalAppointmentDto> {
    List<FullMedicalAppointmentDto> findAllByPatient(String patientDni);
    List<FullMedicalAppointmentDto> findAllByPatientAndSpecialty(String patientDni, String specialityName);
    List<FullMedicalAppointmentDto> findAllByProfessional(String professionalDni);
    List<FullMedicalAppointmentDto> findAllBySpeciality(String specialityName);
    public List<FullMedicalAppointmentDto> findAllByFilters(String specialityName, String professionalDni, LocalDate selectedDate);
    public void updateAppointment(Long id,MedicalAppointment medicalAppointment);
     void updateAppointmentState(Long appointmentId, String newState);
    //Sobre cargo el metodo para poder agregar de manera mas sencilla
    void add(SimpleMedicalAppointmentDto simpleMedicalAppointmentDto);
    void cancel(Long medicalAppointmentId);
    SimpleMedicalAppointmentDto findById(Long medicalAppointmentId);
    public void deleteAppointment(Long appointmentId);
     void reschedule(Long id,MedicalAppointmentDataAllowedToUpdateDto mAllowedToUpdateDto);
     List<FullMedicalAppointmentDto> getMedicalAppointmentsByState(MedicalAppointmentStateEnum appointmentStateEnum);
     List<FullMedicalAppointmentDto> findAllByDate(LocalDate date);
}
