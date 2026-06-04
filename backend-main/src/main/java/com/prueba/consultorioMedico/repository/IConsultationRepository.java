package com.prueba.consultorioMedico.repository;

import com.prueba.consultorioMedico.model.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IConsultationRepository extends JpaRepository<Consultation, Long> {


    @Query("select c from Consultation c order by c.date, c.time desc")
    List<Consultation>findAllOrderByDate();


    @Query("select c from Consultation c where c.specialityName = ?1 order by c.date, c.time asc")
    List<Consultation>findConsultationsBySpecialityOrderByDate(String speciality);

    List<Consultation> findByMedicalRecordId(Long medicalRecordId);
}
