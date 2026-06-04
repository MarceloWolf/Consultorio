package com.prueba.consultorioMedico.exception;

import java.io.Serial;

public class OutOfTimeException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 2L;

    public OutOfTimeException(String message) {
        super(message);
    }
}
