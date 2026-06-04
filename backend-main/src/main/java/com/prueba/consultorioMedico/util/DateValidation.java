package com.prueba.consultorioMedico.util;

import com.prueba.consultorioMedico.exception.DateException;
import com.prueba.consultorioMedico.exception.OutOfServiceException;
import com.prueba.consultorioMedico.exception.OutOfTimeException;
import com.prueba.consultorioMedico.exception.TimeException;
import com.prueba.consultorioMedico.model.Professional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public class DateValidation {

    public static void validateDayOfWeek(LocalDate date) throws OutOfServiceException {
        if (date.getDayOfWeek() == DayOfWeek.SUNDAY)
            throw new OutOfServiceException("Error, la clinica no otorga turnos los dias domingo");
    }

    public static void validateAppointmentTime(LocalTime date) throws OutOfServiceException {
        if (date.isBefore(LocalTime.of(8, 0)) || date.isAfter(LocalTime.of(23, 0)))
            throw new OutOfServiceException("Error, la clinica atiende de 8hs a 23hs");
    }

    // Que los horarios del prfoesional coincidan con el horario de servicio de la
    // clinica
    public static void validateProfessionalTimeOnService(LocalTime start, LocalTime end) throws OutOfServiceException {
        if (start.isBefore(LocalTime.of(8, 0)) || end.isAfter(LocalTime.of(23, 0)))
            throw new OutOfServiceException("Error, la clinica atiende de 8hs a 23hs");
    }

    public static void validateTime(LocalTime start, LocalTime end) throws TimeException {
        if (start.isAfter(end))
            throw new TimeException("Error, horas invalidas");
    }

    public static void validateDateAndTime(LocalDate date, LocalTime time) {
        LocalTime now = LocalTime.now();
        LocalDate today = LocalDate.now();

        if (date.isBefore(today)) {
            throw new DateException("No se puede dar un turno en una fecha posterior a la actual");
        } else if (date.isEqual(today) && time.isBefore(now)) {
            throw new TimeException("No se puede dar un turno en un horario posterior al actual");
        }
    }

    public static void validateProfessionalTime(LocalTime appointmentTime,
            LocalTime professionalStartTime,
            LocalTime professionalEndTime) throws OutOfServiceException {
        if (appointmentTime.isBefore(professionalStartTime) || appointmentTime.isAfter(professionalEndTime))
            throw new OutOfTimeException("Error, el profesional no esta disponible a las " + appointmentTime + "hs");
    }

    public static void validateAbleToModifyOrDelete(LocalTime appointmentTime, LocalDate appointmentDate)
            throws OutOfTimeException {
        LocalTime now = LocalTime.now();
        LocalDate today = LocalDate.now();
        // Si el turno es antes que el dia y horario actual + 1 hora. //Tengo que
        // modificar esta validacion y agregarle la fecha
        if (now.isAfter(appointmentTime.minusHours(1)) && today.isEqual(appointmentDate)) {
            throw new OutOfTimeException(
                    "Error no se puede modificar o eliminar el turno dado a que falta menos de una hora para el mismo");
        }
    }

    public static void validateDateAndTime(LocalDate date, LocalTime time, Professional professional) {

        boolean isDayAvailable = professional.getBusinessDaysList().stream()
        .anyMatch(day -> date.getDayOfWeek().equals(day.getDay()));

        boolean isTimeAvailable = professional.getBusinessDaysList().stream()
        .anyMatch(day -> day.getShift().stream()
        .anyMatch(shift -> shift.getShiftTime().equals(time)));
        
        if(!isTimeAvailable)
        {
            throw new OutOfTimeException("Error, el profesional no esta disponible a las " + time + " hs el dia " + date.getDayOfWeek());
        } 
        else if(!isDayAvailable)
        {
            throw new OutOfTimeException("Error, el profesional no esta disponible el dia " + date.getDayOfWeek());
        }
    }

    //Generar validacion de que si existe el turno no se pueda dar a la misma fecha y hora
    //Podria generar un metodo en el repositorio de MedicalAppointment que busque un turno por una fecha, hora, profesional y paciente 
    //especifico, Si devuelve un resultado es porque ya existe un turno
    //Fecha y hora para que no se repita el horario, el profesional para asegurar que no tenga dos turnos al mismo horario y
    //El paciente para que no pida dos turnos para la misma fecha y horario, ya que no podria asistir a ambos
    //Un problema la validacion tiene que ser con el paciente aparte, ya que si otro paciente quiere cargar una cita con un doctor
    //Que ya tiene planeada una a x horario y fecha se la guardara ya que no coincidiria el dni, por lo tanto debe ser una validacion
    //De Hora, fecha y medico por un lado, y paciente por otro. 
    //EL PACIENTE NO PUEDE PEDIR DOS TURNOS AL MISMO MEDICO EL MISMO DIA Y HORARIO NI TAMPOCO A OTRO MEDICO
    //HAY QUE CORROBORAR LA FECHA Y HORA, Y TODOS LOS MEDICOS

}
