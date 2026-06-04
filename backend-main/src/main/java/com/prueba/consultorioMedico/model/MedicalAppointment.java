package com.prueba.consultorioMedico.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prueba.consultorioMedico.enums.MedicalAppointmentStateEnum;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="medical_appointment")
public class MedicalAppointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medical_appointment_id")
    private Long medicalAppointmentId;

    @ManyToOne
    @JoinColumn(name = "speciality_name")
    @NotNull(message = "La especialidad no puede ser nula.")
    private Speciality speciality;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "patient_dni")
    @NotNull(message = "El paciente no puede ser nulo.")
    private Patient patient;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "professional_dni")
    @NotNull(message = "El profesional no puede ser nulo.")
    private Professional professional;

    @ManyToOne
    @JoinColumn(name = "secretary_dni")
    @NotNull(message = "La secretaria no puede ser nula.")
    private Secretary secretary;

    @Column(nullable = false,name = "appointment_date")
    @NotNull(message = "La fecha de la cita no puede ser nula.")
    private LocalDate appointmentDate;

    @Column(nullable = false,name = "appointment_time")
    @NotNull(message = "La hora de la cita no puede ser nula.")
    private LocalTime appointmentTime;

    @Enumerated(EnumType.STRING)
    private MedicalAppointmentStateEnum state;

    public String getPatientDni() {
        return patient != null ? patient.getDni() : null;
    }

    public String getPatientName() {
        return patient != null ? patient.getName() : null;
    }

    public String getPatientLastname() {
        return patient != null ? patient.getLastname() : null;
    }
    
    public String getProfessionalDni() {
        return professional != null ? professional.getDni() : null;
    }

    public String getProfessionalName() {
        return professional != null ? professional.getName() : null;
    }

    public String getProfessionalLastname() {
        return professional != null ? professional.getLastname() : null;
    }
}
