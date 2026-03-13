package com.jamalkarim.analyzer.entities;

import com.jamalkarim.analyzer.domain.enums.Position;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "player")
public class PlayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int draftPick;
    private boolean isRookie;
    private boolean isInjured;

    @Column(name = "nfl_team")
    private String nflTeam;

    @Enumerated(EnumType.STRING)
    private Position position;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "current_stats_id")
    private StatsEntity currentSeasonStats;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "last_stats_id")
    private StatsEntity lastSeasonStats;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private TeamEntity teamEntity;
}
