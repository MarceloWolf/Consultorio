package com.prueba.consultorioMedico.service.profesionalSpeciality;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prueba.consultorioMedico.dto.SpecialityDto;
import com.prueba.consultorioMedico.model.Professional;
import com.prueba.consultorioMedico.model.ProfessionalSpeciality;
import com.prueba.consultorioMedico.model.Speciality;
import com.prueba.consultorioMedico.repository.IProfessionalRepository;
import com.prueba.consultorioMedico.repository.IProfessionalSpecialityRepository;
import com.prueba.consultorioMedico.repository.ISpecialityRepository;

@Service
@RequiredArgsConstructor
public class ProfessionalSpecialityService implements IProfessionalSpecialityService {
    private final IProfessionalRepository professionalRepository;
    private final ISpecialityRepository specialityRepository;
    private final IProfessionalSpecialityRepository professionalSpecialityRepository;


    @Override
    @Transactional
    public void addSpeciality(String professionalDni, List<SpecialityDto> specialityNames) {
        Professional professional = professionalRepository.findByDNI(professionalDni)
                .orElseThrow(() -> new IllegalArgumentException("Professional not found"));
        specialityNames.stream()
                .forEach(speciality -> {
                    Speciality specialities = specialityRepository.findByName(speciality.getName())
                            .orElseThrow(() -> new IllegalArgumentException("Speciality not found"));
                    ProfessionalSpeciality professionalSpeciality = new ProfessionalSpeciality();
                    professionalSpeciality.setProfessional(professional);
                    professionalSpeciality.setSpeciality(specialities);
            
                    professionalSpecialityRepository.save(professionalSpeciality);
        });
    }

}
