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

    // Vinculamos la entrevista directamente con la postulación
    @OneToOne
    @JoinColumn(name = "postulacion_id", nullable = false)
    private Postulacion postulacion;

    @Column(columnDefinition = "TEXT")
    private String historialConversacion; // Aquí guardaremos el hilo de mensajes del chat

    private String estado; // EN_PROCESO, FINALIZADA

    private LocalDateTime fechaInicio;

    @Column(columnDefinition = "TEXT")
    private String informeFinal; // Evaluación final que emite la IA al terminar
}