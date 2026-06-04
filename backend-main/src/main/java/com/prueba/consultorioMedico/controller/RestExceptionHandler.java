package com.prueba.consultorioMedico.controller;

import com.prueba.consultorioMedico.dto.Message;
import com.prueba.consultorioMedico.exception.*;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.DisabledException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

//En esta clase manejo todas las excepciones y sus respectivas respuestas
@ControllerAdvice
public class RestExceptionHandler {

    // Si la clinica esta fuera de servicio
    @ExceptionHandler(OutOfServiceException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<Message> outOfService(OutOfServiceException e) {
        Message err = new Message(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(err);
    }

    // Si el profesional esta fuera de servicio
    // Si se quiere eliminar la cita y queda 1 hora o menos
    @ExceptionHandler(OutOfTimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Message> outOfTime(OutOfTimeException e) {
        Message err = new Message(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    // Si ingresan un horario invalido
    // Por ejemplo un horario de un profesional que empieza a las 15:00hs y termina
    // a las 9:00
    @ExceptionHandler(TimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Message> invalidTime(TimeException e) {
        Message err = new Message(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(DateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Message> invalidDate(DateException e) {
        Message err = new Message(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(PatientAlreadyHasAppointmentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Message> patientAlreadyHasAppointment(PatientAlreadyHasAppointmentException e) {
        Message err = new Message(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(PatientAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Message> patientAlreadyExist(PatientAlreadyExistException e) {
        Message err = new Message(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(ProfessionalUnavailableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Message> professionalUnavailableException(ProfessionalUnavailableException e) {
        Message err = new Message(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler({ IllegalArgumentException.class, NullPointerException.class, NoSuchElementException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Message> illegalArgumentException(Exception e) {
        Message err = new Message(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Message> dataIntegrityViolation(DataIntegrityViolationException e) {

        String message = e.getMostSpecificCause().getMessage();
        Message err = new Message();

        Pattern pattern = Pattern.compile("Duplicate entry '(.*?)'");
        Matcher matcher = pattern.matcher(message);

        if(matcher.find())
        {
            err = new Message(HttpStatus.BAD_REQUEST, "El valor (" + matcher.group(1) + ") ya se encuentra registrado, porfavor modifiquelo y vuelva a intentar.");
        }
        else
        {
            err = new Message(HttpStatus.BAD_REQUEST, "Error de integridad de datos. Por favor, verifica los datos ingresados.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> errors.put(
                violation.getPropertyPath().toString(),
                violation.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(JpaSystemException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Message> handleConstraintViolation(JpaSystemException ex) {
        Message err = new Message(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Message> HttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Message err = new Message(HttpStatus.BAD_REQUEST, "No se pudo procesar la solicitud. El cuerpo de la solicitud es inválido. Por favor, verifique el formato y los tipos de datos.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Message> ExpiredJwtException(ExpiredJwtException ex) {
        Message err = new Message(HttpStatus.BAD_REQUEST, "Su sesion ha expirado. Porfavor vuelva a ingresar sus credenciales");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Message> handleAuthenticationException(AuthenticationException ex) {
        String msg = "Usuario o contraseña incorrectos";
        if (ex instanceof DisabledException) {
            msg = "El usuario se encuentra dado de baja, por lo tanto no puede ingresar al sistema";
        }
        Message err = new Message(HttpStatus.UNAUTHORIZED, msg);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err);
    }

}
