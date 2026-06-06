package com.prueba.consultorioMedico.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "medical_image")
public class MedicalImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", referencedColumnName = "dni", nullable = false)
    @NotNull(message = "El paciente no puede ser nulo.")
    private Patient patient;

    @Column(name = "file_name", nullable = false)
    @NotBlank(message = "El nombre del archivo no puede estar vacío.")
    private String fileName;

    @Column(name = "file_type", nullable = false)
    @NotBlank(message = "El tipo de archivo (RX/TAC) no puede estar vacío.")
    private String fileType;

    @Column(name = "upload_date", nullable = false)
    private LocalDate uploadDate;

    @Column(length = 1000)
    private String comments;

    @Lob
    @Column(name = "image_data", columnDefinition = "LONGTEXT", nullable = false)
    @NotBlank(message = "Los datos de la imagen no pueden estar vacíos.")
    private String imageData; // Base64 representation of the image
}
