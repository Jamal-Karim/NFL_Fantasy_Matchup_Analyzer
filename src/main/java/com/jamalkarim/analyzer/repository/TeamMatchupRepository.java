package com.jamalkarim.analyzer.repository;

import com.jamalkarim.analyzer.entities.TeamMatchupResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for TeamMatchupResultEntity.
 * Handles persistence for team-to-team comparison reports.
 */
@Repository
public interface TeamMatchupRepository extends JpaRepository<TeamMatchupResultEntity, Long> {
}
