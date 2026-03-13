package com.jamalkarim.analyzer.repository;

import com.jamalkarim.analyzer.entities.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {

    Optional<PlayerEntity> findByNameAndNflTeam(String name, String nflTeam);
}
