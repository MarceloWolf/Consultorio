package com.prueba.consultorioMedico.repository;

import com.prueba.consultorioMedico.enums.MedicalAppointmentStateEnum;
import com.prueba.consultorioMedico.model.MedicalAppointment;
import com.prueba.consultorioMedico.model.Patient;
import com.prueba.consultorioMedico.model.Speciality;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IMedicalAppointmentRepository extends JpaRepository<MedicalAppointment,Long> {
    List<MedicalAppointment> findAllByPatient(Patient patient);
    /* List<MedicalAppointment> findAllByProfessional(Professional professional); */
    /*@Query("SELECT ma FROM MedicalAppointment ma WHERE :speciality MEMBER OF ma.professional.specialityList")
    List<MedicalAppointment> findAllBySpeciality(@Param("speciality") Speciality speciality);*/
    List<MedicalAppointment> findAllBySpeciality(Speciality speciality);

    @Query(value = "SELECT ma.* FROM medical_appointment ma WHERE ma.appointment_date = :date AND ma.appointment_time = :time AND ma.professional_dni = :professionalDni", nativeQuery = true)
    Optional<MedicalAppointment> findAppointmentByProfessional(@Param("date") LocalDate date, @Param("time") LocalTime time, @Param("professionalDni") Long professionalDni);

    @Query(value = "Select ma.* from medical_appointment ma where ma.appointment_date = ?1 AND ma.appointment_time = ?2 AND ma.patient_dni = ?3", nativeQuery = true)
    Optional<MedicalAppointment> findAppointmentByPatient(LocalDate date, LocalTime time, String patientDni);

    @Query("select ma from MedicalAppointment ma join ma.professional p where p.dni = ?1")
    List<MedicalAppointment>findAllMedicalAppointmentByProfessional(String profDni);

    List<MedicalAppointment> findAllByPatientAndSpeciality( Patient patient, Speciality speciality);

    List<MedicalAppointment> findAllByState(MedicalAppointmentStateEnum state);

    @Query("select ma from MedicalAppointment ma where (:specialityName is null or ma.speciality.name = :specialityName) and (:professionalDni is null or ma.professional.dni = :professionalDni) and (:selectedDate is null or ma.appointmentDate = :selectedDate)")
    List<MedicalAppointment> findByFilters(@Param("specialityName") String specialityName, @Param("professionalDni") String professionalDni, @Param("selectedDate") LocalDate selectedDate);

    List<MedicalAppointment> findAllByAppointmentDate(@NotNull(message = "La fecha de la cita no puede ser nula.") LocalDate appointmentDate);
}
