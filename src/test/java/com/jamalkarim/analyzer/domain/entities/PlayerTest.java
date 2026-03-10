package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.PlayerStats;
import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.stats.BlendedStats;
import com.jamalkarim.analyzer.domain.stats.Stats;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    void testPlayerConstructor(){
        Player player = new Player("NFL Player", "NFL Team", Position.QB) {
            @Override
            protected Map<PlayerStats, Impact> generateImpactMap() {
                return Map.of();
            }

            @Override
            public double calculateScareFactor() {
                return 0;
            }

            @Override
            public List<String> generateListOfExplanations() {
                return List.of();
            }
        };

        assertEquals("NFL Player", player.getName(), "The player's name should match the name provided in the constructor.");
        assertEquals("NFL Team", player.getTeam(), "The player's team should match the team provided in the constructor.");
    }

    @Test
    void testCalculateStatBlendStrategyRookie() {
        Player player = new QuarterBack("Rookie", "Team");
        player.setRookie(true);
        player.setDraftPick(1);

        BlendedStats result = player.calculateStatBlendStrategy();
        assertNotNull(result, "The calculated blend strategy result should not be null.");
        assertEquals(211.76, result.getPassingYardsPerGame(), 0.01, "Rookies should use the rookie blend strategy based on draft pick.");
    }

    @Test
    void testCalculateStatBlendStrategyStandard() {
        Player player = new QuarterBack("Standard", "Team");
        Stats last = new Stats();
        last.setGamesPlayed(10);
        last.setPassingYards(3000);
        player.setLastSeasonStats(last);

        Stats current = new Stats();
        current.setGamesPlayed(1);
        current.setPassingYards(300);
        player.setCurrentSeasonStats(current);

        BlendedStats result = player.calculateStatBlendStrategy();
        assertNotNull(result, "The calculated blend strategy result should not be null.");
        assertEquals(300.0, result.getPassingYardsPerGame(), 0.1, "Players with both last and current season data should use the standard blend.");
    }

    @Test
    void testCalculateStatBlendStrategyOnlyLastSeason() {
        Player player = new QuarterBack("LastOnly", "Team");
        Stats last = new Stats();
        last.setGamesPlayed(10);
        last.setPassingYards(3000);
        player.setLastSeasonStats(last);

        BlendedStats result = player.calculateStatBlendStrategy();
        assertEquals(300.0, result.getPassingYardsPerGame(), "Players with only last season data should use a single season blend.");
    }

    @Test
    void testCalculateStatBlendStrategyOnlyCurrentSeason() {
        Player player = new QuarterBack("CurrentOnly", "Team");
        Stats current = new Stats();
        current.setGamesPlayed(5);
        current.setPassingYards(1500);
        player.setCurrentSeasonStats(current);

        BlendedStats result = player.calculateStatBlendStrategy();
        assertEquals(300.0, result.getPassingYardsPerGame(), "Players with only current season data should use a single season blend.");
    }

    @Test
    void testCalculateStatBlendStrategyInjured() {
        Player player = new QuarterBack("Injured", "Team");
        
        BlendedStats result = player.calculateStatBlendStrategy();
        assertEquals(176.47, result.getPassingYardsPerGame(), 0.01, "Injured or data-less players should default to the injured/baseline blend.");
    }

    @Test
    void testTrendGamesBoundary() {
        Player player = new QuarterBack("Boundary", "Team");
        Stats last = new Stats();
        last.setGamesPlayed(6); 
        last.setPassingYards(1800);
        player.setLastSeasonStats(last);

        Stats current = new Stats();
        current.setGamesPlayed(1);
        current.setPassingYards(300);
        player.setCurrentSeasonStats(current);

        BlendedStats result = player.calculateStatBlendStrategy();
        assertEquals(300.0, result.getPassingYardsPerGame(), "Last season stats should be ignored if games played is less than the trend threshold (7).");
    }

    @Test
    void testApplySoftCap() {
        Player player = new QuarterBack("CapTest", "Team");
        
        assertEquals(85.0, player.applySoftCap(85.0), "Scores at exactly 85 should not be capped.");
        assertEquals(50.0, player.applySoftCap(50.0), "Scores below 85 should not be affected by the soft cap.");
        assertEquals(92.5, player.applySoftCap(100.0), 0.01, "Scores above 85 should be progressively capped.");
        assertEquals(99.9, player.applySoftCap(10000.0), 0.01, "Scores should be capped at a maximum of 99.9.");
    }

    @Test
    void testGetTierForStatistic() {
        Player player = new QuarterBack("TierTest", "Team");
        
        assertEquals(1, player.getTierForStatistic(9.5, 10.0), "Value > 90% of weight should be tier 1.");
        assertEquals(2, player.getTierForStatistic(6.5, 10.0), "Value > 60% of weight should be tier 2.");
        assertEquals(3, player.getTierForStatistic(3.0, 10.0), "Value <= 60% of weight should be tier 3.");
        
        assertEquals(-1, player.getTierForStatistic(-9.5, 10.0), "Abs value > 90% of weight should be tier -1.");
        assertEquals(-2, player.getTierForStatistic(-6.5, 10.0), "Abs value > 60% of weight should be tier -2.");
        assertEquals(-3, player.getTierForStatistic(-3.0, 10.0), "Abs value <= 60% of weight should be tier -3.");
    }

    @Test
    void testFindTopContributingScores() {
        Player player = new QuarterBack("TopContrib", "Team");
        
        Map<PlayerStats, Player.Impact> impactMap = new java.util.HashMap<>();
        impactMap.put(PlayerStats.PassingYards, player.new Impact(10.0, 0.0));
        impactMap.put(PlayerStats.PassingTDs, player.new Impact(20.0, 0.0));
        impactMap.put(PlayerStats.Interceptions, player.new Impact(0.0, 30.0)); // Points lost
        impactMap.put(PlayerStats.RushingYards, player.new Impact(5.0, 0.0));

        Map<PlayerStats, Double> topScores = player.findTopContributingScores(impactMap);

        assertEquals(3, topScores.size(), "Should return top 3 contributing factors.");
        
        List<PlayerStats> keys = new java.util.ArrayList<>(topScores.keySet());
        assertEquals(PlayerStats.Interceptions, keys.get(0));
        assertEquals(-30.0, topScores.get(PlayerStats.Interceptions));
        
        assertEquals(PlayerStats.PassingTDs, keys.get(1));
        assertEquals(20.0, topScores.get(PlayerStats.PassingTDs));
        
        assertEquals(PlayerStats.PassingYards, keys.get(2));
        assertEquals(10.0, topScores.get(PlayerStats.PassingYards));
    }
}
