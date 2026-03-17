package com.jamalkarim.analyzer.service;

import com.jamalkarim.analyzer.domain.models.Player;
import com.jamalkarim.analyzer.domain.scoring.ScareResult;
import com.jamalkarim.analyzer.domain.scoring.ScareResultFactory;
import com.jamalkarim.analyzer.dto.response.PlayerResponseDTO;
import com.jamalkarim.analyzer.entities.PlayerEntity;
import com.jamalkarim.analyzer.entities.ScareResultEntity;
import com.jamalkarim.analyzer.exceptions.PlayerNotFoundException;
import com.jamalkarim.analyzer.provider.PlayerDataProvider;
import com.jamalkarim.analyzer.repository.PlayerRepository;
import com.jamalkarim.analyzer.utils.PlayerMapper;
import com.jamalkarim.analyzer.utils.ScareResultMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for managing player-related operations.
 * Handles player retrieval, database persistence, and synchronization with external data providers.
 */
@Service
public class PlayerService {

    private final PlayerRepository repository;
    private final PlayerDataProvider provider;
    private final PlayerMapper playerMapper;
    private final ScareResultMapper scareResultMapper;
    private final ScareResultFactory factory;

    public PlayerService(PlayerRepository repository, PlayerDataProvider provider, PlayerMapper playerMapper, ScareResultMapper scareResultMapper, ScareResultFactory factory) {
        this.repository = repository;
        this.provider = provider;
        this.playerMapper = playerMapper;
        this.scareResultMapper = scareResultMapper;
        this.factory = factory;
    }

    /**
     * Retrieves a player's response DTO by their database ID.
     *
     * @param id The unique identifier of the player
     * @return The player's data transfer object
     * @throws PlayerNotFoundException if no player exists with the given ID
     */
    public PlayerResponseDTO getPlayerResponseDTOByID(long id) {

        Optional<PlayerEntity> player = repository.findById(id);

        if (player.isPresent()) {
            return playerMapper.domainToResponse(playerMapper.entityToDomain(player.get()));
        } else {
            throw new PlayerNotFoundException(id);
        }
    }

    /**
     * Retrieves a player domain object by their database ID.
     *
     * @param id The unique identifier of the player
     * @return The player domain model
     * @throws PlayerNotFoundException if no player exists with the given ID
     */
    public Player getPlayerByID(long id) {

        Optional<PlayerEntity> player = repository.findById(id);

        if (player.isPresent()) {
            return playerMapper.entityToDomain(player.get());
        } else {
            throw new PlayerNotFoundException(id);
        }
    }


    /**
     * Finds a player in the local database or fetches them from the provider if they don't exist.
     * Newly fetched players are saved to the database along with their initial Scare Factor analysis.
     *
     * @param name The name of the player
     * @param team The team they play for
     * @return The player's data transfer object
     * @throws PlayerNotFoundException if the player is not found in the database or the provider
     */
    public PlayerResponseDTO getOrSyncPlayer(String name, String team) {
        Optional<PlayerEntity> player = repository.findByNameAndNflTeam(name, team);
        if (player.isPresent()) {
            return playerMapper.domainToResponse(playerMapper.entityToDomain(player.get()));
        } else {
            Player newPlayer = provider.fetchPlayer(name, team);

            if (newPlayer == null) {
                throw new PlayerNotFoundException(name, team);
            }

            PlayerEntity playerEntity = playerMapper.domainToEntity(newPlayer);

            ScareResult res = factory.generateScareResult(newPlayer);

            ScareResultEntity scareEntity = scareResultMapper.scareDomainToScareEntity(res);

            scareEntity.setPlayer(playerEntity);
            playerEntity.setScareResult(scareEntity);

            PlayerEntity savedEntity = repository.save(playerEntity);
            newPlayer.setId(savedEntity.getId());

            return playerMapper.domainToResponse(newPlayer);
        }
    }
}
