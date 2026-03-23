package com.jamalkarim.analyzer.dto.real;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ExternalDTOMappingTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testAthleteRosterMapping() throws IOException {
        // Given
        InputStream inputStream = new ClassPathResource("real_responses/example_player.json").getInputStream();

        // When
        AthleteExternalDTO.TeamRosterWrapper wrapper = objectMapper.readValue(inputStream, AthleteExternalDTO.TeamRosterWrapper.class);

        // Then
        assertThat(wrapper).isNotNull();
        assertThat(wrapper.getAthletes()).isNotEmpty();

        AthleteExternalDTO athlete = wrapper.getAthletes().get(0);
        assertThat(athlete.getId()).isEqualTo("4595348");
        assertThat(athlete.getName()).isEqualTo("Malik Nabers");
        assertThat(athlete.getPosition().getAbbreviation()).isEqualTo("WR");
        assertThat(athlete.getInjuries()).isNotEmpty();
        assertThat(athlete.getInjuries().get(0).getStatus()).isEqualTo("Questionable");
    }

    @Test
    void testAthleteDetailMapping() throws IOException {
        // Given
        InputStream inputStream = new ClassPathResource("real_responses/example_player_details.json").getInputStream();

        // When
        AthleteExternalDTO.PlayerDetailWrapper wrapper = objectMapper.readValue(inputStream, AthleteExternalDTO.PlayerDetailWrapper.class);

        // Then
        assertThat(wrapper).isNotNull();
        assertThat(wrapper.getAthlete()).isNotNull();

        AthleteExternalDTO athlete = wrapper.getAthlete();
        assertThat(athlete.getId()).isEqualTo("4595348");
        assertThat(athlete.getName()).isEqualTo("Malik Nabers");
        assertThat(athlete.getDraftInfo()).isEqualTo("2024: Rd 1, Pk 6 (NYG)");
        assertThat(athlete.getNflTeam()).isNotNull();
        assertThat(athlete.getNflTeam().getAbbreviation()).isEqualTo("NYG");

        System.out.println(athlete);
    }

    @Test
    void testStatsMapping() throws IOException {
        // Given
        InputStream inputStream = new ClassPathResource("real_responses/example_player_stats.json").getInputStream();

        // When
        StatsExternalDTO statsWrapper = objectMapper.readValue(inputStream, StatsExternalDTO.class);

        // Then
        assertThat(statsWrapper).isNotNull();
        assertThat(statsWrapper.getStatistics()).isNotNull();
        assertThat(statsWrapper.getStatistics().getSplits()).isNotNull();

        List<StatsExternalDTO.CategoryDTO> categories = statsWrapper.getStatistics().getSplits().getCategories();
        assertThat(categories).isNotEmpty();

        // Retrieve "receiving" category
        StatsExternalDTO.CategoryDTO receivingCategory = categories.stream()
                .filter(cat -> "receiving".equals(cat.getName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Receiving category not found"));

        // Assert specific stats for Malik Nabers
        String receptions = getStatValue(receivingCategory, "receptions");
        String yards = getStatValue(receivingCategory, "receivingYards");
        String touchdowns = getStatValue(receivingCategory, "receivingTouchdowns");
        String targets = getStatValue(receivingCategory, "receivingTargets");

        assertThat(receptions).isEqualTo("18");
        assertThat(yards).isEqualTo("271");
        assertThat(touchdowns).isEqualTo("2");
        assertThat(targets).isEqualTo("35");
    }

    private String getStatValue(StatsExternalDTO.CategoryDTO category, String statName) {
        return category.getStatsInCategory().stream()
                .filter(s -> statName.equals(s.getName()))
                .map(StatsExternalDTO.InternalStatsDTO::getValue)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Stat " + statName + " not found in category " + category.getName()));
    }
}
