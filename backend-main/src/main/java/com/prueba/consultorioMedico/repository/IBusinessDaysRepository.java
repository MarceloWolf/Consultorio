package com.prueba.consultorioMedico.repository;

import com.prueba.consultorioMedico.model.BusinessDays;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBusinessDaysRepository extends JpaRepository<BusinessDays, Long> {
}
