package com.prueba.consultorioMedico.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class ProfessionalSpecialitiesDTO {
  FullProfessionalDto professional;
  List<SpecialityDto> specialityNames;
}
