package com.cesarfrade.ats.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfertaResponseDTO {
    private Long id;
    private String titulo;
    private String descripcionPuesto;
    private LocalDateTime fechaPublicacion;
    private boolean activa;
}
