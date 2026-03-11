package com.jamalkarim.analyzer.domain.models;

import com.jamalkarim.analyzer.domain.enums.Position;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a fantasy football team, managing a roster of players
 * and enforcing league-standard roster construction rules.
 * <p>
 * The team enforces limits on specific positions (QB, RB, WR, TE)
 * and supports a single Flex slot for additional RB, WR, or TE players.
 */
@Data
public class Team {

    public static final int MAX_QB = 1;
    public static final int MAX_RB = 2;
    public static final int MAX_WR = 2;
    public static final int MAX_TE = 1;
    public static final int MAX_FLEX = 1;

    private String name;
    private List<Player> roster = new ArrayList<>();

    public Team(String name) {
        this.name = name;
    }

    /**
     * Adds a player to the roster if there is an available slot.
     * <p>
     * The method first attempts to place the player in their primary position slot.
     * If that slot is full, it attempts to place them in the Flex slot (unless they are a QB).
     *
     * @param player The player to add to the team.
     * @throws RuntimeException if the position slot and Flex slot are both full, or if a QB is added when the QB slot is full.
     */
    public void addPlayer(Player player) {

        if (checkCountOfPosition(roster, player.getPosition()) < getLimitOfPosition(player.getPosition())) {
            roster.add(player);
            return;
        }

        if (player.getPosition() == Position.QB) {
            throw new RuntimeException("Max QBs reached. Quarterbacks cannot fill the Flex slot.");
        }

        if (countOverflow(roster) < MAX_FLEX) {
            roster.add(player);
            return;
        }

        throw new RuntimeException("Roster full: Both the " + player.getPosition() + " and Flex slots are occupied.");
    }

    private int getLimitOfPosition(Position position) {
        int limit = 0;
        switch (position) {
            case QB -> limit = MAX_QB;
            case RB -> limit = MAX_RB;
            case WR -> limit = MAX_WR;
            case TE -> limit = MAX_TE;
        }
        return limit;
    }

    private int checkCountOfPosition(List<Player> roster, Position position) {
        int count = 0;
        for (Player player : roster) {
            if (player.getPosition() == position) {
                count++;
            }
        }
        return count;
    }

    private int countOverflow(List<Player> roster) {
        int extras = 0;

        extras += Math.max(0, checkCountOfPosition(roster, Position.RB) - MAX_RB);
        extras += Math.max(0, checkCountOfPosition(roster, Position.WR) - MAX_WR);
        extras += Math.max(0, checkCountOfPosition(roster, Position.TE) - MAX_TE);

        return extras;
    }
}
