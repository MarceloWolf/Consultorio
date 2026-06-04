package com.prueba.consultorioMedico.repository;

import com.prueba.consultorioMedico.model.Professional;
import com.prueba.consultorioMedico.model.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ISpecialityRepository extends JpaRepository<Speciality,Long> {
    //Hago esto porque la relacion es 'vaga'
    @Query("SELECT s FROM Speciality s WHERE :professional MEMBER OF s.professionalList")
    List<Speciality> findAllByProfessional(@Param("professional") Professional professional);

    @Query("SELECT ps.speciality FROM ProfessionalSpeciality ps WHERE ps.professional.dni = :dni")
    List<Speciality> findAllByProfessionalDni(@Param("dni") String dni);


    @Query("Select p from Speciality p where p.name = ?1")
    Optional<Speciality> findByName(String name);

    
    
}
