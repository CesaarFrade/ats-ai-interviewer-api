package com.cesarfrade.ats.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CVResponseDTO {
    private Long id;
    private Long candidatoId;
    private String rutaArchivoS3;
    private String textoCrudo;
}
