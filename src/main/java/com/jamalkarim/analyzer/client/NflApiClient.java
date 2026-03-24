package com.jamalkarim.analyzer.client;

import com.jamalkarim.analyzer.domain.enums.NflTeams;
import com.jamalkarim.analyzer.domain.models.Player;
import com.jamalkarim.analyzer.dto.mock.MockPlayerDTO;
import com.jamalkarim.analyzer.dto.real.AthleteExternalDTO;
import com.jamalkarim.analyzer.dto.real.StatsExternalDTO;
import com.jamalkarim.analyzer.provider.PlayerDataProvider;
import com.jamalkarim.analyzer.utils.PlayerMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class NflApiClient implements PlayerDataProvider {

    private final RestClient restClient;
    private final PlayerMapper playerMapper;

    public NflApiClient(RestClient.Builder builder, PlayerMapper playerMapper) {
        this.restClient = builder
                .baseUrl("https://nfl-api-data.p.rapidapi.com") // Example URL
                .defaultHeader("x-rapidapi-host", "nfl-api-data.p.rapidapi.com")
                .defaultHeader("x-rapidapi-key", "tbd")
                .build();
        this.playerMapper = playerMapper;
    }

    @Override
    public Player fetchPlayer(String name, String nflTeam) {

        AthleteExternalDTO externalPlayer = new AthleteExternalDTO();

        // used to get the specific nfl team id to use in an endpoint
        String teamId = NflTeams.getIdByAbbreviation(nflTeam);

        // get all players on that specific team
        try {
            AthleteExternalDTO.TeamRosterWrapper roster = restClient.get()
                    .uri("/nfl-player-listing/v1/data?id=" + teamId)
                    .retrieve()
                    .body(AthleteExternalDTO.TeamRosterWrapper.class);

            AthleteExternalDTO athlete = null;

            for (AthleteExternalDTO athleteExternalDTO : roster.getAthletes()) {
                if (athleteExternalDTO.getName().equalsIgnoreCase(name)) {
                    athlete = athleteExternalDTO;
                }
            }

            // no athlete found, return null
            if (athlete == null) {
                return null;
            }

            externalPlayer.setId(athlete.getId());
            externalPlayer.setName(athlete.getName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // get specific details for a singular player
        try {
            AthleteExternalDTO.PlayerDetailWrapper playerDetails = restClient.get()
                    .uri("/nfl-ath-fullinfo?id=" + externalPlayer.getId())
                    .retrieve()
                    .body(AthleteExternalDTO.PlayerDetailWrapper.class);

            AthleteExternalDTO athlete = playerDetails.getAthlete();

            externalPlayer.setNflTeam(athlete.getNflTeam());
            externalPlayer.setPosition(athlete.getPosition());
            externalPlayer.setDraftInfo(athlete.getDraftInfo());
            externalPlayer.setInjuries(athlete.getInjuries());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // get specific stats for a player
        // TODO add logic for current year and last year
        try {
            StatsExternalDTO statsWrapper = restClient.get()
                    .uri("/nfl-ath-statistics?year=2025&id=" + externalPlayer.getId())
                    .retrieve()
                    .body(StatsExternalDTO.class);

            externalPlayer.setCurrentSeasonStats(statsWrapper);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        MockPlayerDTO mockPlayerDTO = playerMapper.externalToMock(externalPlayer);
        return playerMapper.entityToDomain(playerMapper.mockToEntity(mockPlayerDTO));
    }
}
