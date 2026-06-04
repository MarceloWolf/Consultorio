package com.prueba.consultorioMedico.exception;

import java.io.Serial;

public class OutOfServiceException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public OutOfServiceException(String message) {
        super(message);
    }
}
