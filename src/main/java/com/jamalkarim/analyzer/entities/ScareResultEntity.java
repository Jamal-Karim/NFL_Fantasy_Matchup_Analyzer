package com.jamalkarim.analyzer.entities;

import com.jamalkarim.analyzer.domain.enums.PlayerTier;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Persistence entity for storing a player's Scare Factor analysis.
 * Links to a PlayerEntity and records their numerical score and tier.
 */
@Data
@Entity
@Table(name = "scare_result")
public class ScareResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "player_id")
    @ToString.Exclude
    private PlayerEntity player;

    private double scareScore;

    @Enumerated(EnumType.STRING)
    private PlayerTier playerTier;

    @Transient
    private String primaryExplanation;

    @Transient
    private List<String> supportingExplanations;
}
