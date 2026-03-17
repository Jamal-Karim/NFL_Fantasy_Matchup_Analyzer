package com.jamalkarim.analyzer.repository;

import com.jamalkarim.analyzer.entities.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for TeamEntity.
 * Handles database operations for NFL fantasy teams.
 */
@Repository
public interface TeamRepository extends JpaRepository<TeamEntity, Long> {

    /**
     * Finds a fantasy team by their name
     *
     * @param name The name of the fantasy team
     * @return An Optional containing the team entity if found
     */
    Optional<TeamEntity> findByName(String name);
}
