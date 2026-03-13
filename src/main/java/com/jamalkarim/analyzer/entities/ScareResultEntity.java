package com.jamalkarim.analyzer.entities;

import com.jamalkarim.analyzer.domain.enums.PlayerTier;
import com.jamalkarim.analyzer.domain.enums.Position;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

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
