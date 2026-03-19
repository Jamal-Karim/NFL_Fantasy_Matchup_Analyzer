package com.jamalkarim.analyzer.repository;

import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.entities.PlayerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Repository interface for PlayerEntity.
 * Handles database operations for NFL players.
 */
@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {

    /**
     * Finds a player by their name and NFL team.
     *
     * @param name    The name of the player
     * @param nflTeam The abbreviation of the player's team
     * @return An Optional containing the player entity if found
     */
    Optional<PlayerEntity> findByNameAndNflTeam(String name, String nflTeam);

    Page<PlayerEntity> findAllByPosition(Position position, Pageable pageable);
}
