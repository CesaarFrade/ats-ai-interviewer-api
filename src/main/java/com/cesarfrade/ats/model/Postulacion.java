package com.cesarfrade.ats.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "postulaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Postulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_candidato")
    private Candidato candidato;

    @ManyToOne
    @JoinColumn(name = "id_oferta")
    private Oferta oferta;

    private Double porcentajeMatch;

    @Column(columnDefinition = "TEXT")
    private String resumenIa;
}