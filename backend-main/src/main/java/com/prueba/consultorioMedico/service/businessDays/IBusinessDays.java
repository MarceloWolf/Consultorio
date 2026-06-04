package com.prueba.consultorioMedico.service.businessDays;

import com.prueba.consultorioMedico.dto.BusinessDaysDto;
import com.prueba.consultorioMedico.model.BusinessDays;
import com.prueba.consultorioMedico.model.Professional;

import java.util.Set;

public interface IBusinessDays {
    Set<BusinessDays> createBusinessDays(Set<BusinessDaysDto> businessDays, Professional professional);

    BusinessDaysDto toDto(BusinessDays businessDays);
}