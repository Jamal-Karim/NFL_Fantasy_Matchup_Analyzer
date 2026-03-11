package com.jamalkarim.analyzer.entities;

import com.jamalkarim.analyzer.domain.enums.Position;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "scare_result")
public class ScareResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "player_id")
    private PlayerEntity player;
    private double scareScore;

}
