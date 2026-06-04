package com.prueba.consultorioMedico.service.speciality;

import com.prueba.consultorioMedico.model.Speciality;
import com.prueba.consultorioMedico.repository.ISpecialityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class SpecialityService implements ISpecialityService {
    private final ISpecialityRepository specialityRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Speciality> findAll() {
        return specialityRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Speciality findByName(String name) {
        return specialityRepository.findByName(name).orElseThrow(()-> new NoSuchElementException("Speciality " + name + " not found"));
    }

    @Override
    @Transactional
    public void add(Speciality speciality) {
        specialityRepository.save(speciality);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Speciality> findSpecialitiesByProfessional(String professionalDni) {
        return specialityRepository.findAllByProfessionalDni(professionalDni);
    }

    @Override
    @Transactional
    public void deleteSpeciality(String specialityName) {
        Speciality speciality = specialityRepository.findByName(specialityName).orElseThrow(()-> new NoSuchElementException("Speciality " + specialityName + " not found"));
        specialityRepository.delete(speciality);
    }
}
