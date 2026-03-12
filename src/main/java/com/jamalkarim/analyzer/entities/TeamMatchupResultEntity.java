package com.jamalkarim.analyzer.entities;

import com.jamalkarim.analyzer.domain.enums.MatchupAdvantages;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "team_matchup")
public class TeamMatchupResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String team1;
    private String team2;
    private double team1TotalScore;
    private double team2TotalScore;
    private double team1Probability;
    private double team2Probability;

    @Enumerated(EnumType.STRING)
    private MatchupAdvantages advantage;

    @OneToMany(mappedBy = "teamMatchupResult", cascade = CascadeType.ALL)
    private List<PlayerMatchupResultEntity> playerMatchupResults;

}
