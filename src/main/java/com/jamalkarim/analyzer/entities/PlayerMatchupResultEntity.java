package com.jamalkarim.analyzer.entities;

import com.jamalkarim.analyzer.domain.enums.MatchupAdvantages;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "player_matchup")
public class PlayerMatchupResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_1_id")
    @ToString.Exclude
    private PlayerEntity player1;

    @ManyToOne
    @JoinColumn(name = "player_2_id")
    @ToString.Exclude
    private PlayerEntity player2;

    private double scareDifference;

    @Enumerated(EnumType.STRING)
    private MatchupAdvantages advantage;

    @Transient
    private String explanation;

    @ManyToOne
    @JoinColumn(name = "winner_id")
    private PlayerEntity winner;

    @ManyToOne
    @JoinColumn(name = "loser_id")
    private PlayerEntity loser;

    @ManyToOne
    @JoinColumn(name = "player_1_scare_result_id")
    private ScareResultEntity player1ScareResult;

    @ManyToOne
    @JoinColumn(name = "player_2_scare_result_id")
    @ToString.Exclude
    private ScareResultEntity player2ScareResult;

    @ManyToOne
    @JoinColumn(name = "team_matchup_id")
    @ToString.Exclude
    private TeamMatchupResultEntity teamMatchupResult;

}
