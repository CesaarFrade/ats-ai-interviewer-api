package com.cesarfrade.ats.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostulacionRequestDTO {
    @NotNull(message = "El id del candidato es obligatorio")
    private Long candidatoId;

    @NotNull(message = "El id de la oferta es obligatorio")
    private Long ofertaId;

    private Double porcentajeMatch;
    private String resumenIa;
}
