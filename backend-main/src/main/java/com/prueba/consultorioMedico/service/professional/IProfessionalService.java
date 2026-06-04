package com.prueba.consultorioMedico.service.professional;


import java.util.List;

import com.prueba.consultorioMedico.dto.FullProfessionalDto;
import com.prueba.consultorioMedico.dto.SimpleProfessionalDto;
import com.prueba.consultorioMedico.enums.AccountStateEnum;
import com.prueba.consultorioMedico.model.MedicalAppointment;
import com.prueba.consultorioMedico.model.Professional;


public interface IProfessionalService{
    List<MedicalAppointment> findMedicalAppointmentByProfessional(String profDni);
    List<Professional> findAll();
    void updateProfessional(SimpleProfessionalDto professionalDto, String dni);
    List<SimpleProfessionalDto> findAllByAccountState(AccountStateEnum accountStateEnum);
    List<FullProfessionalDto> findAllBySpeciality(String speciality);
    Professional findByDNI(String dni);
    FullProfessionalDto findFullProfessionalByDNI(String dni);
    void deleteProfessionalByDNI(String dni);
}
