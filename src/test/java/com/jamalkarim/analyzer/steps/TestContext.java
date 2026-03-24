package com.jamalkarim.analyzer.steps;

import com.jamalkarim.analyzer.dto.response.PlayerResponseDTO;
import io.restassured.response.Response;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class TestContext {
    private Response response;
    private PlayerResponseDTO player;
}
