package com.prueba.consultorioMedico.exception;

import java.io.Serial;

public class PatientAlreadyHasAppointmentException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 6L;

    public PatientAlreadyHasAppointmentException(String message) {
        super(message);
    }
}
