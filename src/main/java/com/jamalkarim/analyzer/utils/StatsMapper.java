package com.jamalkarim.analyzer.utils;

import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.stats.Stats;
import com.jamalkarim.analyzer.dto.mock.MockStatsDTO;
import com.jamalkarim.analyzer.dto.real.StatsExternalDTO;
import com.jamalkarim.analyzer.entities.StatsEntity;

import java.util.List;

/**
 * Mapper utility for player statistics.
 * Handles conversions between mock DTOs, JPA entities, domain models, and API response DTOs.
 */
public class StatsMapper {

    private int zeroIfNull(Integer value) {
        return value == null ? 0 : value;
    }

    /**
     * Converts a database entity to its domain model.
     *
     * @param statsEntity The database entity
     * @return The domain model
     */
    public Stats entityToDomain(StatsEntity statsEntity) {
        Stats stats = new Stats();

        stats.setSeason(statsEntity.getSeason());
        stats.setGamesPlayed(statsEntity.getGamesPlayed());

        stats.setPassAttempts(statsEntity.getPassAttempts());
        stats.setCompletions(statsEntity.getCompletions());
        stats.setPassingYards(statsEntity.getPassingYards());
        stats.setPassingTDs(statsEntity.getPassingTDs());
        stats.setInterceptions(statsEntity.getInterceptions());

        stats.setRushingAttempts(statsEntity.getRushingAttempts());
        stats.setRushingYards(statsEntity.getRushingYards());
        stats.setRushingTDs(statsEntity.getRushingTDs());

        stats.setReceptions(statsEntity.getReceptions());
        stats.setReceivingYards(statsEntity.getReceivingYards());
        stats.setReceivingTDs(statsEntity.getReceivingTDs());

        return stats;
    }

    /**
     * Converts a domain model to a database entity.
     *
     * @param domainStats The domain model
     * @return A database entity
     */
    public StatsEntity domainToEntity(Stats domainStats) {

        if (domainStats == null) {
            return null;
        }

        StatsEntity stats = new StatsEntity();

        stats.setSeason(domainStats.getSeason());
        stats.setGamesPlayed(domainStats.getGamesPlayed());

        stats.setPassAttempts(domainStats.getPassAttempts());
        stats.setCompletions(domainStats.getCompletions());
        stats.setPassingYards(domainStats.getPassingYards());
        stats.setPassingTDs(domainStats.getPassingTDs());
        stats.setInterceptions(domainStats.getInterceptions());

        stats.setRushingAttempts(domainStats.getRushingAttempts());
        stats.setRushingYards(domainStats.getRushingYards());
        stats.setRushingTDs(domainStats.getRushingTDs());

        stats.setReceptions(domainStats.getReceptions());
        stats.setReceivingYards(domainStats.getReceivingYards());
        stats.setReceivingTDs(domainStats.getReceivingTDs());

        return stats;
    }

    /**
     * Converts a MockStatsDTO to a database entity.
     *
     * @param mockStatsDTO The mock data transfer object
     * @return A database entity
     */
    public StatsEntity mockToEntity(MockStatsDTO mockStatsDTO) {

        if (mockStatsDTO == null) {
            return null;
        }

        StatsEntity stats = new StatsEntity();

        stats.setSeason(zeroIfNull(mockStatsDTO.getSeason()));
        stats.setGamesPlayed(zeroIfNull(mockStatsDTO.getGamesPlayed()));

        stats.setPassAttempts(zeroIfNull(mockStatsDTO.getPassAttempts()));
        stats.setCompletions(zeroIfNull(mockStatsDTO.getCompletions()));
        stats.setPassingYards(zeroIfNull(mockStatsDTO.getPassingYards()));
        stats.setPassingTDs(zeroIfNull(mockStatsDTO.getPassingTDs()));
        stats.setInterceptions(zeroIfNull(mockStatsDTO.getInterceptions()));

        stats.setRushingAttempts(zeroIfNull(mockStatsDTO.getRushingAttempts()));
        stats.setRushingYards(zeroIfNull(mockStatsDTO.getRushingYards()));
        stats.setRushingTDs(zeroIfNull(mockStatsDTO.getRushingTDs()));

        stats.setReceptions(zeroIfNull(mockStatsDTO.getReceptions()));
        stats.setReceivingYards(zeroIfNull(mockStatsDTO.getReceivingYards()));
        stats.setReceivingTDs(zeroIfNull(mockStatsDTO.getReceivingTDs()));

        return stats;
    }

    /**
     * Converts a mock statistical DTO to a domain stats object.
     *
     * @param mockStatsDTO The mock data transfer object
     * @return A domain stats object
     */
    public Stats mockToDomain(MockStatsDTO mockStatsDTO) {
        Stats stats = new Stats();

        stats.setSeason(zeroIfNull(mockStatsDTO.getSeason()));
        stats.setGamesPlayed(zeroIfNull(mockStatsDTO.getGamesPlayed()));

        stats.setPassAttempts(zeroIfNull(mockStatsDTO.getPassAttempts()));
        stats.setCompletions(zeroIfNull(mockStatsDTO.getCompletions()));
        stats.setPassingYards(zeroIfNull(mockStatsDTO.getPassingYards()));
        stats.setPassingTDs(zeroIfNull(mockStatsDTO.getPassingTDs()));
        stats.setInterceptions(zeroIfNull(mockStatsDTO.getInterceptions()));

        stats.setRushingAttempts(zeroIfNull(mockStatsDTO.getRushingAttempts()));
        stats.setRushingYards(zeroIfNull(mockStatsDTO.getRushingYards()));
        stats.setRushingTDs(zeroIfNull(mockStatsDTO.getRushingTDs()));

        stats.setReceptions(zeroIfNull(mockStatsDTO.getReceptions()));
        stats.setReceivingYards(zeroIfNull(mockStatsDTO.getReceivingYards()));
        stats.setReceivingTDs(zeroIfNull(mockStatsDTO.getReceivingTDs()));

        return stats;
    }

    /**
     * Converts a domain model to a MockStatsDTO.
     *
     * @param domainStats The domain model
     * @param position    The player's position to determine which stats are relevant
     * @return A mock data transfer object
     */
    public MockStatsDTO domainToMock(Stats domainStats, Position position) {

        if (domainStats == null) {
            return null;
        }

        MockStatsDTO stats = new MockStatsDTO();

        stats.setSeason(domainStats.getSeason());
        stats.setGamesPlayed(domainStats.getGamesPlayed());

        if (position == Position.QB) {
            stats.setPassAttempts(domainStats.getPassAttempts());
            stats.setCompletions(domainStats.getCompletions());
            stats.setPassingYards(domainStats.getPassingYards());
            stats.setPassingTDs(domainStats.getPassingTDs());
            stats.setInterceptions(domainStats.getInterceptions());
            stats.setRushingAttempts(domainStats.getRushingAttempts());
            stats.setRushingYards(domainStats.getRushingYards());
            stats.setRushingTDs(domainStats.getRushingTDs());
        }

        if (position == Position.RB) {
            stats.setRushingAttempts(domainStats.getRushingAttempts());
            stats.setRushingYards(domainStats.getRushingYards());
            stats.setRushingTDs(domainStats.getRushingTDs());

            stats.setReceptions(domainStats.getReceptions());
            stats.setReceivingYards(domainStats.getReceivingYards());
            stats.setReceivingTDs(domainStats.getReceivingTDs());
        }

        if (position == Position.WR || position == Position.TE) {
            stats.setReceptions(domainStats.getReceptions());
            stats.setReceivingYards(domainStats.getReceivingYards());
            stats.setReceivingTDs(domainStats.getReceivingTDs());

        }
        return stats;
    }

    public MockStatsDTO externalToMock(StatsExternalDTO externalStats, Position position, int season) {

        if (externalStats == null) {
            return null;
        }

        StatsExternalDTO.SplitsContainer container = externalStats.getStatistics().getSplits();
        List<StatsExternalDTO.CategoryDTO> categories = container.getCategories();
        MockStatsDTO stats = new MockStatsDTO();
        stats.setSeason(container.getSeason());

        StatsExternalDTO.CategoryDTO generalCategory = categories.stream()
                .filter(cat -> "general".equals(cat.getName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("General category not found"));

        StatsExternalDTO.CategoryDTO passingCategory = categories.stream()
                .filter(cat -> "passing".equals(cat.getName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Passing category not found"));

        StatsExternalDTO.CategoryDTO rushingCategory = categories.stream()
                .filter(cat -> "rushing".equals(cat.getName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Rushing category not found"));

        StatsExternalDTO.CategoryDTO receivingCategory = categories.stream()
                .filter(cat -> "receiving".equals(cat.getName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Receiving category not found"));

        Integer gamesPlayed = getStatValue(generalCategory, "gamesPlayed");

        Integer passAttempts = getStatValue(passingCategory, "passingAttempts");
        Integer completions = getStatValue(passingCategory, "completions");
        Integer passingYards = getStatValue(passingCategory, "passingYards");
        Integer passingTDs = getStatValue(passingCategory, "passingTouchdowns");
        Integer interceptions = getStatValue(passingCategory, "interceptions");

        Integer rushingAttempts = getStatValue(rushingCategory, "rushingAttempts");
        Integer rushingYards = getStatValue(rushingCategory, "rushingYards");
        Integer rushingTDs = getStatValue(rushingCategory, "rushingTouchdowns");

        Integer receptions = getStatValue(receivingCategory, "receptions");
        Integer receivingYards = getStatValue(receivingCategory, "receivingYards");
        Integer receivingTouchdowns = getStatValue(receivingCategory, "receivingTouchdowns");

        stats.setSeason(season);
        stats.setGamesPlayed(gamesPlayed);

        if (position == Position.QB) {
            stats.setPassAttempts(passAttempts);
            stats.setCompletions(completions);
            stats.setPassingYards(passingYards);
            stats.setPassingTDs(passingTDs);
            stats.setInterceptions(interceptions);
            stats.setRushingAttempts(rushingAttempts);
            stats.setRushingYards(rushingYards);
            stats.setRushingTDs(rushingTDs);
        }

        if (position == Position.RB) {
            stats.setRushingAttempts(rushingAttempts);
            stats.setRushingYards(rushingYards);
            stats.setRushingTDs(rushingTDs);

            stats.setReceptions(receptions);
            stats.setReceivingYards(receivingYards);
            stats.setReceivingTDs(receivingTouchdowns);
        }

        if (position == Position.WR || position == Position.TE) {
            stats.setReceptions(receptions);
            stats.setReceivingYards(receivingYards);
            stats.setReceivingTDs(receivingTouchdowns);

        }

        return stats;
    }

    private Integer getStatValue(StatsExternalDTO.CategoryDTO category, String statName) {
        String stat = category.getStatsInCategory().stream().filter(s -> statName.equals(s.getName()))
                .map(StatsExternalDTO.InternalStatsDTO::getValue)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Stat " + statName + " not found in category " + category.getName()));
        return Integer.valueOf(stat);
    }
}