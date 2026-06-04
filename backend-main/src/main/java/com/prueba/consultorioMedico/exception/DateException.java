package com.prueba.consultorioMedico.exception;

import java.io.Serial;

public class DateException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 4L;

    public DateException(String message) {
        super(message);
    }
}
