package com.jamalkarim.analyzer.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Persistence entity representing a team.
 * Holds a roster of player entities.
 */
@Getter
@Setter
@Entity
@Table(name = "team")
public class TeamEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "teamEntity", cascade = CascadeType.ALL)
    private List<PlayerEntity> roster = new ArrayList<>();

    public void addPlayer(PlayerEntity player) {
        this.roster.add(player);
        player.setTeamEntity(this);
    }
}
