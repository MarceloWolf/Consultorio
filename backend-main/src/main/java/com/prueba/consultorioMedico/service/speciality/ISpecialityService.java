package com.prueba.consultorioMedico.service.speciality;

import com.prueba.consultorioMedico.model.Speciality;
import com.prueba.consultorioMedico.service.IGenericService;

import java.util.List;

public interface ISpecialityService extends IGenericService<Speciality> {
    List<Speciality> findSpecialitiesByProfessional(String professionalDni);
     Speciality findByName(String name);
     public void deleteSpeciality(String specialityName);
}
