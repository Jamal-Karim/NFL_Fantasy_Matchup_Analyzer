package com.jamalkarim.analyzer.service;

import com.jamalkarim.analyzer.domain.models.Player;
import com.jamalkarim.analyzer.domain.scoring.ScareResult;
import com.jamalkarim.analyzer.domain.scoring.ScareResultFactory;
import com.jamalkarim.analyzer.entities.PlayerEntity;
import com.jamalkarim.analyzer.entities.ScareResultEntity;
import com.jamalkarim.analyzer.provider.PlayerDataProvider;
import com.jamalkarim.analyzer.repository.PlayerRepository;
import com.jamalkarim.analyzer.utils.PlayerMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerService {

    private final PlayerRepository repository;
    private final PlayerDataProvider provider;
    private final PlayerMapper playerMapper;

    public PlayerService(PlayerRepository repository, PlayerDataProvider provider, PlayerMapper playerMapper) {
        this.repository = repository;
        this.provider = provider;
        this.playerMapper = playerMapper;
    }

    public Player getOrSyncPlayer(String name, String team) {
        Optional<PlayerEntity> player = repository.findByNameAndNflTeam(name, team);
        if (player.isPresent()) {
            return playerMapper.entityToDomain(player.get());
        } else {
            Player newPlayer = provider.fetchPlayer(name, team);

            PlayerEntity playerEntity = playerMapper.domainToEntity(newPlayer);

            ScareResultFactory factory = new ScareResultFactory();
            ScareResult res = factory.generateScareResult(newPlayer);

            ScareResultEntity scareEntity = playerMapper.scareDomainToScareEntity(res);

            scareEntity.setPlayer(playerEntity);
            playerEntity.setScareResult(scareEntity);

            repository.save(playerEntity);
            return newPlayer;
        }
    }
}
