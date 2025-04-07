package com.faculdade.reservasala.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "reservas")
public class Reserva {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long salaId;
    
    @Column(nullable = false)
    private Long usuarioId;
    
    @Column(nullable = false)
    private LocalDateTime dataHoraInicio;
    
    @Column(nullable = false)
    private LocalDateTime dataHoraFim;
    
    @Column(nullable = false)
    private String motivo;
    
    @Column(nullable = false)
    private String status; // PENDENTE, CONFIRMADA, CANCELADA
} 