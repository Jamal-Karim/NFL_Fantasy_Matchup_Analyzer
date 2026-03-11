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
    private String NFLTeam;

    @Enumerated(EnumType.STRING)
    private Position position;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "stats_id")
    private StatsEntity stats;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private TeamEntity teamEntity;
}
