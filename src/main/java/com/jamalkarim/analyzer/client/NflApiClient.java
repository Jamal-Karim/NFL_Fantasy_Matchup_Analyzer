package com.jamalkarim.analyzer.client;

import com.jamalkarim.analyzer.domain.enums.NflTeams;
import com.jamalkarim.analyzer.domain.models.Player;
import com.jamalkarim.analyzer.dto.mock.MockPlayerDTO;
import com.jamalkarim.analyzer.dto.real.AthleteExternalDTO;
import com.jamalkarim.analyzer.dto.real.StatsExternalDTO;
import com.jamalkarim.analyzer.provider.PlayerDataProvider;
import com.jamalkarim.analyzer.utils.PlayerMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;

@Service
@Primary
public class NflApiClient implements PlayerDataProvider {

    private final RestClient restClient;
    private final PlayerMapper playerMapper;
    private static final int currentYear = LocalDate.now().getYear();
    private static final int lastYear = currentYear - 1;

    public NflApiClient(RestClient.Builder builder, PlayerMapper playerMapper,
                        @Value("${rapidapi.host}") String apiHost,
                        @Value("${rapidapi.key}") String apiKey) {
        this.restClient = builder
                .baseUrl("https://nfl-api-data.p.rapidapi.com") // Example URL
                .defaultHeader("x-rapidapi-host", apiHost)
                .defaultHeader("x-rapidapi-key", apiKey)
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


            for (AthleteExternalDTO.RosterGroup group : roster.getAthletes()) {
                for (AthleteExternalDTO athleteInGroup : group.getItems()) {
                    if (athleteInGroup.getName().equalsIgnoreCase(name)) {
                        athlete = athleteInGroup;
                        break;
                    }
                }
                if (athlete != null) break;
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
        try {
            StatsExternalDTO statsWrapper = restClient.get()
                    .uri("/nfl-ath-statistics?year=" + currentYear + "&id=" + externalPlayer.getId())
                    .retrieve()
                    .body(StatsExternalDTO.class);

            if (statsWrapper != null &&
                    statsWrapper.getStatistics() != null &&
                    statsWrapper.getStatistics().getSplits() != null) {

                externalPlayer.setCurrentSeasonStats(statsWrapper);
                externalPlayer.getCurrentSeasonStats().getStatistics().getSplits().setSeason(currentYear);
            }
        } catch (Exception e) {
            System.out.println("No stats found for " + currentYear);
        }

        try {
            StatsExternalDTO statsWrapper = restClient.get()
                    .uri("/nfl-ath-statistics?year=" + lastYear + "&id=" + externalPlayer.getId())
                    .retrieve()
                    .body(StatsExternalDTO.class);

            if (statsWrapper != null &&
                    statsWrapper.getStatistics() != null &&
                    statsWrapper.getStatistics().getSplits() != null) {

                externalPlayer.setLastSeasonStats(statsWrapper);
                externalPlayer.getLastSeasonStats().getStatistics().getSplits().setSeason(lastYear);
            }
        } catch (Exception e) {
            System.out.println("No stats found for " + lastYear);
        }

        MockPlayerDTO mockPlayerDTO = playerMapper.externalToMock(externalPlayer);
        return playerMapper.entityToDomain(playerMapper.mockToEntity(mockPlayerDTO));
    }
}
