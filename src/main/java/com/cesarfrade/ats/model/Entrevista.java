package com.cesarfrade.ats.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "entrevistas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Entrevista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "postulacion_id", nullable = false)
    private Postulacion postulacion;

    @Column(columnDefinition = "TEXT")
    private String historialConversacion;

    private String estado;

    private LocalDateTime fechaInicio;

    @Column(columnDefinition = "TEXT")
    private String informeFinal;
}
