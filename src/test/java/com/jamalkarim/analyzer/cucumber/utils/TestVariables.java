package com.jamalkarim.analyzer.cucumber.utils;

import com.jamalkarim.analyzer.dto.response.PlayerResponseDTO;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * In-memory storage for variables shared between steps in a Cucumber scenario.
 * Supports resolution of '{key}' syntax for dynamic data sharing.
 */
@Component
public class TestVariables {

    private final Map<String, Object> map = new HashMap<>();

    /**
     * Adds a player DTO to the map using their name as the key.
     */
    public void addPlayerToMap(PlayerResponseDTO playerDTO) {
        map.put(playerDTO.getName(), playerDTO);
    }

    /**
     * Retrieves a player DTO by their name.
     */
    public PlayerResponseDTO getPlayer(String name) {
        if (map.containsKey(name)) {
            return (PlayerResponseDTO) map.get(name);
        } else {
            throw new TestVariableException("Player not found: " + name);
        }
    }

    /**
     * Retrieves a saved value by its variable key (e.g., id1).
     *
     * @param key The key to look up (must be wrapped in curly braces or previously resolved)
     */
    public Object getKey(String key) {
        String extractedKey = extractKey(key);

        if (map.containsKey(extractedKey)) {
            return map.get(extractedKey);
        } else {
            throw new TestVariableException("Key not found: " + key);
        }
    }

    /**
     * Saves a value to the map.
     */
    public void fillSafely(String key, Object value) {
        String extractedKey = extractKey(key);
        map.put(extractedKey, value);
    }

    /**
     * Clears all saved variables.
     */
    public void clearAll() {
        map.clear();
    }

    /**
     * Resolves a value by checking if it's a saved variable (wrapped in {})
     * or a literal string.
     *
     * @param input The string to resolve (e.g., "{id1}" or "123")
     * @return The resolved value as a String
     */
    public String resolve(String input) {
        if (input.startsWith("{") && input.endsWith("}")) {
            return String.valueOf(getKey(input));
        }
        return input;
    }

    /**
     * Safely saves an ID to a variable name (must be wrapped in {}).
     */
    public void saveIdToVariable(String variableName, Object id) {
        fillSafely(variableName, id);
    }

    /**
     * Helper to remove curly braces from a variable key.
     */
    private String extractKey(String key) {
        if (!key.startsWith("{") || !key.endsWith("}")) {
            throw new TestVariableException("Invalid key format, key must be wrapped in curly braces {key}: " + key);
        }
        return key.substring(1, key.length() - 1);
    }
}
