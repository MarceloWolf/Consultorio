package com.prueba.consultorioMedico.exception;

import java.io.Serial;

public class TimeException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 3L;

    public TimeException(String message) {
        super(message);
    }
}
