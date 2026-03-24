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

    /**
     * Adds a player to the team roster and sets the back-reference.
     *
     * @param player The player entity to add
     */
    public void addPlayer(PlayerEntity player) {
        this.roster.add(player);
        player.setTeamEntity(this);
    }

    /**
     * Removes a player from the team roster and clears the back-reference.
     *
     * @param player The player entity to remove
     */
    public void removePlayer(PlayerEntity player) {
        this.roster.remove(player);
        player.setTeamEntity(null);
    }

    /**
     * Clears all players from the roster and removes their association with this team.
     */
    public void clearRoster() {
        List<PlayerEntity> players = new ArrayList<>(this.roster);
        for (PlayerEntity player : players) {
            removePlayer(player);
        }
    }

}
