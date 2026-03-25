package com.jamalkarim.analyzer.cucumber.steps;

import com.jamalkarim.analyzer.dto.response.PlayerResponseDTO;
import com.jamalkarim.analyzer.dto.response.ScareResponseDTO;
import com.jamalkarim.analyzer.dto.response.TeamResponseDTO;
import io.restassured.response.Response;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class TestContext {
    private Response response;
    private PlayerResponseDTO playerResponse;
    private ScareResponseDTO scareResponse;
    private TeamResponseDTO teamResponseDTO;

    public void clear() {
        this.response = null;
        this.playerResponse = null;
        this.scareResponse = null;
    }
}
