package com.jamalkarim.analyzer.domain.stats;

import com.jamalkarim.analyzer.domain.entities.Player;
import com.jamalkarim.analyzer.domain.enums.PlayerTier;
import com.jamalkarim.analyzer.domain.enums.Position;
import lombok.Data;

import java.util.List;

@Data
public class ScareResult {

    private Player player;
    private String name;
    private String team;
    private Position position;
    private double scareScore;
    private PlayerTier scareTier;
    private String primaryExplanation;
    private List<String> supportingExplanations;
    private ScareResultFactory scareResultFactory = new ScareResultFactory();

    public ScareResult(Player player) {
        this.player = player;
        this.name = player.getName();
        this.team = player.getTeam();
        this.position = player.getPosition();
    }
}
