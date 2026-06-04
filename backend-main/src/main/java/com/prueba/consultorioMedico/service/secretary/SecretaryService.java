package com.prueba.consultorioMedico.service.secretary;

import java.util.List;
import java.util.NoSuchElementException;

import com.prueba.consultorioMedico.dto.SecretaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prueba.consultorioMedico.enums.AccountStateEnum;
import com.prueba.consultorioMedico.model.Secretary;
import com.prueba.consultorioMedico.repository.ISecretaryRepository;

@Service
@RequiredArgsConstructor
public class SecretaryService implements ISecretaryService{
    private final ISecretaryRepository secretaryRepository;



    @Override
    @Transactional(readOnly = true)
    public List<Secretary> findAll() {
        return  secretaryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Secretary> findAllByAccountState(AccountStateEnum accountStateEnum) {
        return secretaryRepository.findAllByAccountState(accountStateEnum);
    }


    @Override
    @Transactional(readOnly = true)
    public Secretary findByDNI(String dni) {
        return secretaryRepository.findByDni(dni)
                .orElseThrow(() ->new NoSuchElementException("Secretario/a con DNI: " +
                        dni + " no fue encontrado"));
    }

    @Override
    @Transactional
    public void deleteSecretary(String dni) {
        Secretary secretary = secretaryRepository.findByDni(dni)
                .orElseThrow(() ->new NoSuchElementException("Secretario/a con DNI: " +
                        dni + " no fue encontrado"));
        secretary.setAccountState(AccountStateEnum.INACTIVE);
        secretaryRepository.save(secretary);
    }

    @Override
    @Transactional
    public void updateSecretary(SecretaryDto secretaryDto, String dni) {
        Secretary secretary = secretaryRepository.findByDni(dni)
                .orElseThrow(() ->new NoSuchElementException("Secretario/a con DNI: " +
                        dni + " no fue encontrado"));

        secretary.setName(secretaryDto.getName());
        secretary.setLastname(secretaryDto.getLastname());
        secretary.setDni(secretaryDto.getDni());
        secretary.setUsername(secretaryDto.getUsername());
        secretary.setPhoneNumber(secretaryDto.getPhoneNumber());
        secretary.setAddress(secretaryDto.getAddress());
        secretary.setEmail(secretaryDto.getEmail());
        secretary.setRole(secretaryDto.getRole());
        secretary.setAccountState(secretaryDto.getAccountState());
        secretaryRepository.save(secretary);
    }


}
