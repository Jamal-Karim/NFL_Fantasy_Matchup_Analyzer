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

    Optional<TeamMatchupResultEntity> findByTeam1AndTeam2(String team1, String team2);
}
