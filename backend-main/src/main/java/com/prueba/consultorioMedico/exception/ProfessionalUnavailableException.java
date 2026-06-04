package com.prueba.consultorioMedico.exception;

import java.io.Serial;

public class ProfessionalUnavailableException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 5L;

    public ProfessionalUnavailableException(String message) {
        super(message);
    }
}
