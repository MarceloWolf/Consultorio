package com.prueba.consultorioMedico.repository;

import com.prueba.consultorioMedico.model.MedicalRecord;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IMedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

	@Query("Select mr from MedicalRecord mr where mr.patient.dni = ?1 ")
	Optional<MedicalRecord> findByPatientDni(String patientDni);

}
