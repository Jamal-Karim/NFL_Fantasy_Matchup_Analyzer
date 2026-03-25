package com.jamalkarim.analyzer.cucumber.utils;

import com.jamalkarim.analyzer.dto.response.PlayerResponseDTO;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TestVariables {

    private final Map<String, Object> map = new HashMap<>();

    public void addPlayerToMap(PlayerResponseDTO playerDTO) {
        map.put(playerDTO.getName(), playerDTO);
    }

    public PlayerResponseDTO getPlayer(String name) {
        if (map.containsKey(name)) {
            return (PlayerResponseDTO) map.get(name);
        } else {
            throw new RuntimeException("Player not found");
        }
    }

    public Object getKey(String key) {
        String extractedKey = extractKey(key);

        if (map.containsKey(extractedKey)) {
            return map.get(extractedKey);
        } else {
            throw new RuntimeException("Key not found");
        }
    }

    public void fillSafely(String key, Object value) {
        String extractedKey = extractKey(key);
        map.put(extractedKey, value);
    }

    private String extractKey(String key) {
        if (!key.startsWith("{") || !key.endsWith("}")) {
            throw new RuntimeException("Invalid key format, key must be wrapped in curly braces {key}");
        }
        return key.substring(1, key.length() - 1);
    }
}
