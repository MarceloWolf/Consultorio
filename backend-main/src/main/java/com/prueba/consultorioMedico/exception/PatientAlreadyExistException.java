package com.prueba.consultorioMedico.exception;

import java.io.Serial;

public class PatientAlreadyExistException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 7L;

    public PatientAlreadyExistException(String message) {
        super(message);
    }
}
