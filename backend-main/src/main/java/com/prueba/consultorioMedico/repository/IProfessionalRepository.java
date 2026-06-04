package com.prueba.consultorioMedico.repository;

import com.prueba.consultorioMedico.enums.AccountStateEnum;
import com.prueba.consultorioMedico.model.Professional;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IProfessionalRepository extends JpaRepository<Professional,Long> {
    @Query("select p from Professional p where p.dni = ?1")
    Optional<Professional> findByDNI(String dni);
    @Query("select p from Professional p where p.accountState = ?1")
    List<Professional> findAllByAccountState(AccountStateEnum accountStateEnum);
    @Query("SELECT p FROM Professional p " +
            "LEFT JOIN FETCH p.businessDaysList bd " +
            "LEFT JOIN FETCH bd.shift s WHERE s.isShiftReserved = false AND EXISTS (SELECT ps FROM ProfessionalSpeciality ps WHERE ps.professional = p AND ps.speciality.name = :speciality)" )
    List<Professional> findAllBySpecialityName(String speciality);
}
