package com.jamalkarim.analyzer.repository;

import com.jamalkarim.analyzer.entities.TeamMatchupResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for TeamMatchupResultEntity.
 * Handles persistence for team-to-team comparison reports.
 */
@Repository
public interface TeamMatchupRepository extends JpaRepository<TeamMatchupResultEntity, Long> {

    /**
     * Finds a stored team matchup result between two specific teams.
     *
     * @param team1 The name of the first team
     * @param team2 The name of the second team
     * @return An Optional containing the matchup result if it exists
     */
    Optional<TeamMatchupResultEntity> findByTeam1AndTeam2(String team1, String team2);
}
