package com.prueba.consultorioMedico.service.secretary;


import java.util.List;


import com.prueba.consultorioMedico.dto.SecretaryDto;
import com.prueba.consultorioMedico.enums.AccountStateEnum;
import com.prueba.consultorioMedico.model.Secretary;

public interface ISecretaryService{
    List<Secretary> findAll();
    List<Secretary> findAllByAccountState(AccountStateEnum accountStateEnum);
    Secretary findByDNI(String dni);
    void deleteSecretary(String dni);
    void updateSecretary(SecretaryDto secretaryDto, String dni);
}
