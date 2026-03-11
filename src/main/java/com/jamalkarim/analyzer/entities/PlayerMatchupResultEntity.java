package com.jamalkarim.analyzer.entities;

import com.jamalkarim.analyzer.domain.enums.MatchupAdvantages;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "player_matchup")
public class PlayerMatchupResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "player_1_id")
    private PlayerEntity player1;

    @OneToOne
    @JoinColumn(name = "player_2_id")
    private PlayerEntity player2;

    private double scareDifference;

    @Enumerated(EnumType.STRING)
    private MatchupAdvantages advantage;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    @OneToOne
    @JoinColumn(name = "player_1_scare_result_id")
    private ScareResultEntity player1ScareResult;

    @OneToOne
    @JoinColumn(name = "player_2_scare_result_id")
    private ScareResultEntity player2ScareResult;

}
