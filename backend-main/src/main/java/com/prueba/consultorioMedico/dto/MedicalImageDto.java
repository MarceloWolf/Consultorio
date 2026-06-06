package com.prueba.consultorioMedico.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicalImageDto {
    private String fileName;
    private String fileType;
    private String comments;
    private String imageData;
}
