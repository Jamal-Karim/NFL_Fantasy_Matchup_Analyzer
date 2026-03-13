package com.jamalkarim.analyzer.repository;

import com.jamalkarim.analyzer.entities.PlayerMatchupResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerMatchupRepository extends JpaRepository<PlayerMatchupResultEntity, Long> {
}
