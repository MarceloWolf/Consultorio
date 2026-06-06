package com.prueba.consultorioMedico.service.toothState;

import com.prueba.consultorioMedico.model.ToothState;

import java.util.List;

public interface IToothStateService {
    List<ToothState> getToothStatesByPatient(String patientDni);
    ToothState saveOrUpdateToothState(String patientDni, Integer toothNumber, String state, String notes);
}
