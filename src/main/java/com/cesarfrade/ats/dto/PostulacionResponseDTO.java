package com.cesarfrade.ats.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostulacionResponseDTO {
    private Long id;
    private Long candidatoId;
    private Long ofertaId;
    private Double porcentajeMatch;
    private String resumenIa;
}
