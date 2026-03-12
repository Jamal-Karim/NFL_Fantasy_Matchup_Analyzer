package com.jamalkarim.analyzer.domain.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TeamTest {

    private Team team;

    @BeforeEach
    void setUp() {
        team = new Team("Test Team");
    }

    @Test
    @DisplayName("Should add players within primary position limits")
    void shouldAddPlayersWithinPrimaryLimits() {
        Player qb = new QuarterBack("QB1", "TeamA");
        Player rb1 = new RunningBack("RB1", "TeamA");
        Player rb2 = new RunningBack("RB2", "TeamA");
        Player wr1 = new WideReceiver("WR1", "TeamA");
        Player wr2 = new WideReceiver("WR2", "TeamA");
        Player te = new TightEnd("TE1", "TeamA");

        team.addPlayer(qb);
        team.addPlayer(rb1);
        team.addPlayer(rb2);
        team.addPlayer(wr1);
        team.addPlayer(wr2);
        team.addPlayer(te);

        assertThat(team.getRoster())
                .as("Roster should contain all 6 starters within primary limits")
                .hasSize(6)
                .contains(qb, rb1, rb2, wr1, wr2, te);
    }

    @Test
    @DisplayName("Should add a player to Flex if primary position is full")
    void shouldAddPlayerToFlexIfPrimaryFull() {
        team.addPlayer(new RunningBack("RB1", "TeamA"));
        team.addPlayer(new RunningBack("RB2", "TeamA"));

        Player flexRb = new RunningBack("RB3", "TeamA");
        team.addPlayer(flexRb);

        assertThat(team.getRoster())
                .as("Roster should contain 3 RBs (2 standard, 1 flex)")
                .hasSize(3)
                .contains(flexRb);
    }

    @Test
    @DisplayName("Should throw exception if QB is added when QB slot is full (Flex not allowed)")
    void shouldThrowExceptionWhenAddingExtraQB() {
        team.addPlayer(new QuarterBack("QB1", "TeamA"));

        Player extraQb = new QuarterBack("QB2", "TeamA");

        assertThatThrownBy(() -> team.addPlayer(extraQb))
                .as("Should throw exception because QBs cannot fill the Flex slot")
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Quarterbacks cannot fill the Flex slot");
    }

    @Test
    @DisplayName("Should throw exception if Roster is full including Flex")
    void shouldThrowExceptionWhenRosterFull() {
        // Fill RB, WR, TE slots
        team.addPlayer(new RunningBack("RB1", "TeamA"));
        team.addPlayer(new RunningBack("RB2", "TeamA"));
        team.addPlayer(new WideReceiver("WR1", "TeamA"));
        team.addPlayer(new WideReceiver("WR2", "TeamA"));
        team.addPlayer(new TightEnd("TE1", "TeamA"));

        team.addPlayer(new RunningBack("RB3", "TeamA"));

        Player extraRb = new RunningBack("RB4", "TeamA");

        assertThatThrownBy(() -> team.addPlayer(extraRb))
                .as("Should throw exception because both RB and Flex slots are full")
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Roster full");
    }
}
