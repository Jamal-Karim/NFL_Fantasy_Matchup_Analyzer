package com.jamalkarim.analyzer.domain.scoring;

import com.jamalkarim.analyzer.domain.entities.Player;
import com.jamalkarim.analyzer.domain.enums.PlayerTier;
import com.jamalkarim.analyzer.domain.enums.Position;
import lombok.Data;

import java.util.List;

/**
 *  Object that encapsulates the final results of a
 * Scare Factor analysis.
 * 
 * Contains the numerical score, the categorical tier, and textual 
 * explanations for display in reports or UIs.
 */
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

    public ScareResult(Player player) {
        this.player = player;
        this.name = player.getName();
        this.team = player.getTeam();
        this.position = player.getPosition();
    }

    public void printScareReport() {
        String separator = "------------------------------------------";

        System.out.println("\n" + separator);
        System.out.print(this.name.toUpperCase());
        System.out.println(" | " + this.team.toUpperCase());
        System.out.println(this.position + " | Scare Factor: " + (int)this.scareScore);
        System.out.println("Tier: " + this.scareTier);
        System.out.println(separator);

        System.out.println("PRIMARY REASON:");
        System.out.println(this.primaryExplanation);
        System.out.println();

        if (this.supportingExplanations != null && !this.supportingExplanations.isEmpty()) {
            System.out.println("SUPPORTING REASONING:");
            for (String factor : this.supportingExplanations) {
                System.out.println("• " + factor);
            }
        }

        System.out.println(separator + "\n");
    }
}
