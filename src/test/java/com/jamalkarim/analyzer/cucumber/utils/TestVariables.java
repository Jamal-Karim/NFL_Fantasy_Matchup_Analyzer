package com.jamalkarim.analyzer.cucumber.utils;

import com.jamalkarim.analyzer.dto.response.PlayerResponseDTO;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TestVariables {

    private final Map<String, Object> map = new HashMap<>();

    public void addPlayerToMap(PlayerResponseDTO playerDTO) {
        if (map.containsKey(playerDTO.getName())) {
            throw new RuntimeException("Player already exists");
        }
        map.put(playerDTO.getName(), playerDTO);
    }

    public PlayerResponseDTO getPlayer(String name) {
        if (map.containsKey(name)) {
            return (PlayerResponseDTO) map.get(name);
        } else {
            throw new RuntimeException("Player not found");
        }
    }
}
