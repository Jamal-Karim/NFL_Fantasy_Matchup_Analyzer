package com.jamalkarim.analyzer.utils;

import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.models.*;
import com.jamalkarim.analyzer.dto.mock.MockPlayerDTO;
import com.jamalkarim.analyzer.dto.mock.MockStatsDTO;
import com.jamalkarim.analyzer.dto.real.AthleteExternalDTO;
import com.jamalkarim.analyzer.dto.response.PlayerResponseDTO;
import com.jamalkarim.analyzer.dto.response.RosterMemberDTO;
import com.jamalkarim.analyzer.entities.PlayerEntity;
import com.jamalkarim.analyzer.entities.StatsEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper utility for converting Player-related objects between different layers.
 * Handles conversions between Mock DTOs, JPA Entities, Domain Models, and API Responses.
 */
@Component
public class PlayerMapper {

    private final StatsMapper statsMapper = new StatsMapper();

    /**
     * Converts a PlayerEntity to its corresponding domain model.
     *
     * @param playerEntity The database entity
     * @return A concrete instance of Player
     */
    public Player entityToDomain(PlayerEntity playerEntity) {
        Position position = playerEntity.getPosition();

        Player domainPlayer = switch (position) {
            case QB -> new QuarterBack(playerEntity.getName(), playerEntity.getNflTeam());
            case RB -> new RunningBack(playerEntity.getName(), playerEntity.getNflTeam());
            case WR -> new WideReceiver(playerEntity.getName(), playerEntity.getNflTeam());
            case TE -> new TightEnd(playerEntity.getName(), playerEntity.getNflTeam());
        };

        domainPlayer.setId(playerEntity.getId());
        domainPlayer.setDraftPick(playerEntity.getDraftPick());
        domainPlayer.setRookie(playerEntity.isRookie());
        domainPlayer.setInjured(playerEntity.isInjured());

        if (playerEntity.getCurrentSeasonStats() != null) {
            domainPlayer.setCurrentSeasonStats(statsMapper.entityToDomain(playerEntity.getCurrentSeasonStats()));
        }

        if (playerEntity.getLastSeasonStats() != null) {
            domainPlayer.setLastSeasonStats(statsMapper.entityToDomain(playerEntity.getLastSeasonStats()));
        }

        return domainPlayer;
    }

    /**
     * Converts a MockPlayerDTO to a PlayerEntity.
     *
     * @param mockPlayerDTO The mock data transfer object
     * @return A database entity
     */
    public PlayerEntity mockToEntity(MockPlayerDTO mockPlayerDTO) {
        PlayerEntity player = new PlayerEntity();

        player.setName(mockPlayerDTO.getName());
        player.setNflTeam(mockPlayerDTO.getNflTeam());
        player.setPosition(mockPlayerDTO.getPosition());

        StatsEntity currentStats = statsMapper.mockToEntity(mockPlayerDTO.getCurrentSeasonStats());
        StatsEntity lastStats = statsMapper.mockToEntity(mockPlayerDTO.getLastSeasonStats());
        player.setCurrentSeasonStats(currentStats);
        player.setLastSeasonStats(lastStats);

        player.setDraftPick(mockPlayerDTO.getDraftPick());
        player.setRookie(mockPlayerDTO.isRookie());
        player.setInjured(mockPlayerDTO.isInjured());

        return player;
    }

    /**
     * Converts a Player domain model to a PlayerEntity.
     *
     * @param player The domain model
     * @return A database entity
     */
    public PlayerEntity domainToEntity(Player player) {
        PlayerEntity playerEntity = new PlayerEntity();

        playerEntity.setName(player.getName());
        playerEntity.setNflTeam(player.getTeam());
        playerEntity.setPosition(player.getPosition());

        StatsEntity currentStats = statsMapper.domainToEntity(player.getCurrentSeasonStats());
        StatsEntity lastStats = statsMapper.domainToEntity(player.getLastSeasonStats());
        playerEntity.setCurrentSeasonStats(currentStats);
        playerEntity.setLastSeasonStats(lastStats);

        playerEntity.setDraftPick(player.getDraftPick());
        playerEntity.setRookie(player.isRookie());
        playerEntity.setInjured(player.isInjured());

        return playerEntity;
    }

    /**
     * Converts a Player domain model to an API response DTO.
     *
     * @param player The domain model
     * @return An API response DTO
     */
    public PlayerResponseDTO domainToResponse(Player player) {
        PlayerResponseDTO playerResponseDTO = new PlayerResponseDTO();

        playerResponseDTO.setId(player.getId());
        playerResponseDTO.setName(player.getName());
        playerResponseDTO.setNflTeam(player.getTeam());
        playerResponseDTO.setPosition(player.getPosition());
        playerResponseDTO.setRookie(player.isRookie());
        playerResponseDTO.setInjured(player.isInjured());
        playerResponseDTO.setCurrentSeasonStats(statsMapper.domainToMock(player.getCurrentSeasonStats(), player.getPosition()));
        playerResponseDTO.setLastSeasonStats(statsMapper.domainToMock(player.getLastSeasonStats(), player.getPosition()));

        return playerResponseDTO;
    }

    /**
     * Converts an API response DTO to a Player domain model.
     *
     * @param responseDTO The API response DTO
     * @return A concrete instance of Player
     */
    public Player responseToDomain(PlayerResponseDTO responseDTO) {

        Position position = responseDTO.getPosition();

        Player domainPlayer = switch (position) {
            case QB -> new QuarterBack(responseDTO.getName(), responseDTO.getNflTeam());
            case RB -> new RunningBack(responseDTO.getName(), responseDTO.getNflTeam());
            case WR -> new WideReceiver(responseDTO.getName(), responseDTO.getNflTeam());
            case TE -> new TightEnd(responseDTO.getName(), responseDTO.getNflTeam());
        };

        domainPlayer.setId(responseDTO.getId());
        domainPlayer.setRookie(responseDTO.isRookie());
        domainPlayer.setInjured(responseDTO.isInjured());

        if (responseDTO.getCurrentSeasonStats() != null) {
            domainPlayer.setCurrentSeasonStats(statsMapper.mockToDomain(responseDTO.getCurrentSeasonStats()));
        }

        if (responseDTO.getLastSeasonStats() != null) {
            domainPlayer.setLastSeasonStats(statsMapper.mockToDomain(responseDTO.getLastSeasonStats()));
        }

        return domainPlayer;
    }

    /**
     * Converts a Player domain model to a simplified RosterMemberDTO.
     *
     * @param player The player domain model
     * @return A RosterMemberDTO containing key player info
     */
    public RosterMemberDTO domainToRosterMember(Player player) {
        RosterMemberDTO rosterMemberDTO = new RosterMemberDTO();
        rosterMemberDTO.setId(player.getId());
        rosterMemberDTO.setName(player.getName());
        rosterMemberDTO.setPosition(player.getPosition());
        rosterMemberDTO.setNflTeam(player.getTeam());
        return rosterMemberDTO;
    }

    public RosterMemberDTO entityToRosterMember(PlayerEntity player) {
        RosterMemberDTO rosterMemberDTO = new RosterMemberDTO();
        rosterMemberDTO.setId(player.getId());
        rosterMemberDTO.setName(player.getName());
        rosterMemberDTO.setPosition(player.getPosition());
        rosterMemberDTO.setNflTeam(player.getNflTeam());
        return rosterMemberDTO;
    }

    public MockPlayerDTO externalToMock(AthleteExternalDTO externalPlayer) {
        MockPlayerDTO player = new MockPlayerDTO();

        player.setName(externalPlayer.getName());
        player.setNflTeam(externalPlayer.getNflTeam().getAbbreviation());
        player.setPosition(Position.valueOf(externalPlayer.getPosition().getAbbreviation()));

        player.setInjured(externalPlayer.hasActiveInjuries());
        player.setDraftPick(externalPlayer.getDraftPosition(externalPlayer.getDraftInfo()));
        player.setRookie(externalPlayer.isRookie(externalPlayer.getDraftInfo()));

        player.setCurrentSeasonStats(statsMapper.externalToMock(externalPlayer.getCurrentSeasonStats(), player.getPosition(), 2025));
        player.setLastSeasonStats(statsMapper.externalToMock(externalPlayer.getLastSeasonStats(), player.getPosition(), 2025));

        return player;
    }
}
