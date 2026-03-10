package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.Position;

import java.util.ArrayList;
import java.util.List;

public class Team {

    public static final int MAX_QB = 1;
    public static final int MAX_RB = 2;
    public static final int MAX_WR = 2;
    public static final int MAX_TE = 1;
    public static final int MAX_FLEX = 1;

    private String name;
    private int wins;
    private List<Player> roster = new ArrayList<>();

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
