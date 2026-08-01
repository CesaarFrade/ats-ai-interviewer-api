package com.cesarfrade.ats.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "curriculums")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CV {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_candidato")
    private Candidato candidato;

    @Column(nullable = false, length = 500)
    private String ruta_archivo_S3;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String texto_crudo;
}
