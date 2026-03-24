package com.jamalkarim.analyzer.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamalkarim.analyzer.domain.enums.NflTeams;
import com.jamalkarim.analyzer.domain.models.Player;
import com.jamalkarim.analyzer.dto.mock.MockPlayerDTO;
import com.jamalkarim.analyzer.dto.real.AthleteExternalDTO;
import com.jamalkarim.analyzer.dto.real.StatsExternalDTO;
import com.jamalkarim.analyzer.utils.PlayerMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Component
@Primary
public class NflApiProvider implements PlayerDataProvider {

    private final ObjectMapper objectMapper;
    private final PlayerMapper playerMapper;
    private final String[] jsonFiles = {"real_responses/example_player.json",
            "real_responses/example_player_details.json", "real_responses/example_player_stats.json"};

    public NflApiProvider(ObjectMapper objectMapper, PlayerMapper playerMapper) {
        this.objectMapper = objectMapper;
        this.playerMapper = playerMapper;
    }

    @Override
    public Player fetchPlayer(String name, String nflTeam) {

        AthleteExternalDTO externalPlayer = new AthleteExternalDTO();

        // would be used to get the specific nfl team id to use in an endpoint, not needed for mock testing
        String teamId = NflTeams.getIdByAbbreviation(nflTeam);

        try {


            String allPlayersOnTeam = jsonFiles[0];

            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(allPlayersOnTeam)) {

                AthleteExternalDTO.TeamRosterWrapper wrapper = objectMapper.readValue(inputStream, AthleteExternalDTO.TeamRosterWrapper.class);
                AthleteExternalDTO athlete = wrapper.getAthletes().get(0);

                externalPlayer.setId(athlete.getId());
                externalPlayer.setName(athlete.getName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String playerDetails = jsonFiles[1];

            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(playerDetails)) {
                AthleteExternalDTO.PlayerDetailWrapper wrapper = objectMapper.readValue(inputStream, AthleteExternalDTO.PlayerDetailWrapper.class);
                AthleteExternalDTO athlete = wrapper.getAthlete();

                externalPlayer.setNflTeam(athlete.getNflTeam());
                externalPlayer.setPosition(athlete.getPosition());
                externalPlayer.setDraftInfo(athlete.getDraftInfo());
                externalPlayer.setInjuries(athlete.getInjuries());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String statsDetails = jsonFiles[2];

            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(statsDetails)) {

                StatsExternalDTO statsWrapper = objectMapper.readValue(inputStream, StatsExternalDTO.class);
                externalPlayer.setCurrentSeasonStats(statsWrapper);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            MockPlayerDTO mockPlayerDTO = playerMapper.externalToMock(externalPlayer);
            return playerMapper.entityToDomain(playerMapper.mockToEntity(mockPlayerDTO));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
