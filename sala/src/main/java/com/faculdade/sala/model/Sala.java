package com.faculdade.sala.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "salas")
public class Sala {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false)
    private Integer capacidade;
    
    @Column(nullable = false)
    private String localizacao;
    
    @Column(nullable = false)
    private Boolean disponivel;
} 