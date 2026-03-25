package com.jamalkarim.analyzer.cucumber.steps;

import com.jamalkarim.analyzer.dto.response.*;
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
    private PlayerMatchupResponseDTO playerMatchupResponseDTO;
    private TeamMatchupResponseDTO teamMatchupResponseDTO;

    public void clear() {
        this.response = null;
        this.playerResponse = null;
        this.scareResponse = null;
        this.teamResponseDTO = null;
        this.playerMatchupResponseDTO = null;
        this.teamMatchupResponseDTO = null;
    }
}
