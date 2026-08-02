package com.cesarfrade.ats.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CVRequestDTO {
    @NotNull(message = "El id del candidato es obligatorio")
    private Long candidatoId;

    @NotBlank(message = "La ruta del archivo en S3 es obligatoria")
    private String rutaArchivoS3;

    @NotBlank(message = "El texto crudo del currículum es obligatorio")
    private String textoCrudo;
}
