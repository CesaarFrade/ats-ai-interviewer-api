package com.cesarfrade.ats.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidatoResponseDTO {
    private Long id;
    private String nombreCandidato;
    private String email;
    private String telefono;
    private LocalDateTime fechaRegistro;
}
