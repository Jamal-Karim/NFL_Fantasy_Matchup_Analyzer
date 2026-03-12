package com.jamalkarim.analyzer.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "stats")
public class StatsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int season;
    private int gamesPlayed;

    private int passAttempts;
    private int completions;
    private int passingYards;
    private int passingTDs;
    private int interceptions;

    private int rushingAttempts;
    private int rushingYards;
    private int rushingTDs;

    private int targets;
    private int receptions;
    private int receivingYards;
    private int receivingTDs;
}
