package com.prueba.consultorioMedico.service.profesionalSpeciality;

import java.util.List;

import com.prueba.consultorioMedico.dto.SpecialityDto;


public interface IProfessionalSpecialityService{

    void addSpeciality(String professionalDni, List<SpecialityDto> specialityNames);

}
