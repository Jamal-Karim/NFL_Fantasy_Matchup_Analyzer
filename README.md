# 🏈 NFL Fantasy Analyzer

A backend system for probabilistic fantasy football analysis, combining a custom scoring model with Monte Carlo
simulation to evaluate matchup outcomes, player performance, and risk.

## ❗ Problem

Most fantasy football tools rely on a single projected point value for each player. These projections don’t capture how
consistent or volatile a player is, forcing users to manually infer risk from averages alone.

As a result, players with identical projections can have completely different performance profiles, making it
difficult to make informed decisions.

## 💡 Solution

This system introduces a custom scoring model called **Scare Factor**, designed to quantify player impact and
performance volatility

Scare Factor is computed using:

- historical and current performance data
- positional context and role
- comparisons against elite positional baselines (2023–2025 seasons)

To move beyond static projections, the system also incorporates a **Monte Carlo simulation** to model performance
variability and generate probabilistic outcomes.

For team-level analysis, individual player scores are aggregated to evaluate overall team strength.

Each analysis includes generated explanations, helping users understand *why* one player or team is favored.

## ✨ Key Features

- **Scare Factor scoring model** for measuring player impact and volatility
- **Monte Carlo simulation engine (10,000 iterations)** for probabilistic performance modeling
- Player vs Player and Team vs Team matchup evaluation
- Aggregated team analysis based on individual player scoring
- Dynamically generated explanations for all matchup results
- RESTful APIs for player, team, and matchup analysis
- Fantasy team creation and roster management

## 🧱 System Architecture

The system is designed around a layered architecture with a pluggable data provider system and a sync-on-demand
retrieval strategy.

<details>
<summary><b>Click to expand: System Architecture Diagram</b></summary>
<br>

```mermaid
graph TD
subgraph API_Layer [Web API Layer]
PC[PlayerController]
TC[TeamController]
MC[MatchupController]
end

    subgraph Service_Layer [Business Logic & Orchestration]
        PS[PlayerService]
        TS[TeamService]
        MS[Matchup Services]
        SS[SimulationService]
    end

    subgraph Analysis_Engine [Domain Analysis Engine]
        direction LR
        Analyzers[Matchup Analyzers]
        Scare[Scare Factor Logic]
        Models[Domain Models]
    end

    subgraph Data_Orchestration [Data Access & Sync]
        PDP[PlayerDataProvider]
        Repo[JPA Repositories]
    end

    subgraph Infrastructure [External & Storage]
        direction LR
        API[External NFL API]
        Mock[Mock JSON Provider]
        DB[(H2/Relational DB)]
    end

    %% Flows
    API_Layer --> Service_Layer
    
    Service_Layer --> Analysis_Engine
    Service_Layer --> Data_Orchestration
    
    Analysis_Engine --> Data_Orchestration
    
    Data_Orchestration --> Infrastructure
    PDP -.-> API & Mock
    Repo -.-> DB
```

</details>

### 🔁 Data Flow

1. A request is received by a controller
2. Service layer retrieves player data using a **sync-on-demand strategy**
3. Data is loaded from the database if present, otherwise fetched from an external API
4. Domain models perform analysis (scoring, matchup evaluation)
5. Results are mapped to response DTOs and returned to the client

**For a detailed technical look at the internal logic, see our documentation:**

* [Data Retrieval Flowchart](./docs/diagrams/data-providers.md) — Visualizes the Sync-on-Demand logic.
* [Request Lifecycle Sequence](./docs/diagrams/team-matchup-request-lifecycle.md) — End-to-end API execution flow.

### 🧠 Key Design Decisions

- **Scare Factor metric:** Created to quantify player impact and volatility beyond traditional projections
- **Sync-on-demand data strategy:** Ensures high availability by dynamically fetching and persisting player stats only
  when local records are missing
- **Monte Carlo simulation:** Models uncertainty and distribution of outcomes rather than relying on static averages
- **Pluggable data providers:** Supports both mock and external APIs for testability and flexibility
- **Layered architecture:** Enforces separation of concerns for maintainability and scalability
- **Profile-based configuration:** Enables different environments without code changes

### 🧩 Data Providers

The system supports multiple data sources:

- **Mock Provider:** Uses preloaded JSON files with 100+ players for consistent local testing
- **External API Provider:** Fetches real-time data from a third-party NFL API (rate-limited)

This design allows for reliable development while still supporting real-world data integration.

## 🎲 Simulation Engine

The system includes a Monte Carlo simulation endpoint for player performance analysis.

To move beyond static projections, the system utilizes a Monte Carlo simulation to model performance probability.

* **Logic:** Runs 10,000 iterations per request, applying position-specific Gaussian variance to reflect real-world
  volatility.
* **Outcomes:** Calculates probabilistic Floor, Ceiling, and Median outcomes to identify "Boom/Bust" potential.
* **Deep Dive:** [View Simulation Engine Logic & Flowchart](./docs/diagrams/monte-carlo-simulation.md)

## 🗄️ Data Model

Key entities in the system include:

- **Player:** Represents an NFL player and their performance statistics
- **Team:** A collection of players forming a fantasy roster
- **Scare Result:** An analysis report of a singular player
- **Player Matchup:** Represents a head-to-head comparison between two players
- **Team Matchup:** Represents an aggregate comparison between two teams

## 🧪 Testing Strategy

The system uses a multi-layered testing approach:

- **Unit Tests:** Validate core domain logic and scoring calculations
- **Integration Tests:** Ensure end-to-end functionality across services and controllers
- **Cucumber (BDD):** Models real-world user scenarios, including edge cases and invalid inputs

This ensures both correctness of logic and reliability of API workflows.

## 📈 Model Evaluation (2025 Fantasy Season)

### Overview

- **Total team matchups evaluated:** 24
- **Total player matchups evaluated:** 72
- **Prediction accuracy:** 58.33%

### Confidence Calibration

| Confidence Level    | Accuracy |
|:--------------------|:---------|
| **High (>80%)**     | 55.56%   |
| **Medium (60–80%)** | 54.55%   |
| **Low (<60%)**      | 75.00%   |

> The model demonstrated higher accuracy in low-confidence matchups, indicating that high-confidence predictions may
> overestimate certainty in a highly volatile environment.

![Model Accuracy by Confidence Level](./docs/analysis/Model%20Accuracy%20by%20Confidence%20Level.png)

### Comparison to Traditional Projections

- **App outperformed platform:** 6 matchups
- **Platform outperformed app:** 7 matchups
- **Both correct:** 8 matchups
- **Both incorrect:** 3 matchups

![Model App vs Platform](./docs/analysis/Model%20Performance%20of%20Application%20vs%20Platform.png)

Overall, the system matched or outperformed traditional projections in **58%** of matchups, demonstrating competitive
performance.

### Player vs Team-Level Performance

- **Average player-level accuracy:** 50.75%
- **Team-level accuracy:** 58.33%

While individual player predictions were near baseline, aggregating them into team-level analysis improved overall
accuracy, validating the system’s design.

### Failure Analysis

Most incorrect predictions were attributed to:

1. Player injuries or unexpected performance drops
2. Missing positional data (kicker/defense)
3. Extreme boom/bust performances
4. Giving too much scare factor potential to really volatile players

These factors highlight the inherent volatility of fantasy football and areas for future improvement.

## ⚙️ DevOps & Infrastructure

- **Docker:** Containerizes the application and MySQL database for consistent local environments
- **Docker Compose:** Orchestrates multi-container setup for local development
- **Jenkins:** Automates build and test pipelines for continuous integration
- **Spring Profiles:** Enables environment-based configuration
- **Database Switching:** Uses MySQL for development and H2 for automated testing

This setup enables reproducible environments, automated validation, and efficient local development workflows.

## 📊 API Documentation

The backend exposes a set of RESTful APIs for player analysis, simulations, and matchup evaluation.

Interactive API documentation is available via Swagger UI once the application is running:
`http://localhost:8081/swagger-ui.html`

### Key Endpoints

| Category    | Method | Endpoint                                    | Description                      |
|:------------|:-------|:--------------------------------------------|:---------------------------------|
| **Player**  | `GET`  | `/api/player/team/{team}?name={playerName}` | Retrieve player by name and team |
| **Player**  | `GET`  | `/api/player/{id}/analysis`                 | Get Scare Factor analysis        |
| **Player**  | `GET`  | `/api/player/{id}/simulation`               | Run Monte Carlo simulation       |
| **Team**    | `POST` | `/api/team/create`                          | Create a fantasy team            |
| **Matchup** | `POST` | `/api/matchup/player/create`                | Compare two players              |
| **Matchup** | `POST` | `/api/matchup/team/create`                  | Compare two teams                |

### Key API Examples

<details>
<summary><b>Player Analysis</b> (<code>GET /api/player/{id}/analysis</code>)</summary>

```json
{
  "status": "SUCCESS",
  "data": {
    "name": "Bijan Robinson",
    "nfl_team": "ATL",
    "position": "RB",
    "scare_score": 92.41,
    "scare_tier": "ELITE",
    "primary_explanation": "Highly productive lead runner with strong overall metrics.",
    "supporting_explanations": [
      "Lethal red zone threat; consistently finishes drives.",
      "Elite receiving back; a vital mismatch in the passing game."
    ]
  }
}
```

</details>

<details>
<summary><b>Monte Carlo Simulation</b> (<code>GET /api/player/{id}/simulation</code>)</summary>

```json
{
  "status": "SUCCESS",
  "message": "Operation successful",
  "timestamp": "2026-03-30T22:07:32.681319300Z",
  "data": {
    "name": "Jaylen Warren",
    "nfl_team": "PIT",
    "position": "RB",
    "scare_tier": "AVERAGE",
    "avg_scare_score": 60.43,
    "best_scare_score": 72.9,
    "worst_scare_score": 50.63,
    "boom_percentage": 11.32,
    "bust_percentage": 2.24
  }
}
```

</details>

<details>
<summary><b>Player Matchup Result</b> (<code>POST /api/matchup/player/create</code>)</summary>

```json
{
  "status": "SUCCESS",
  "message": "Operation successful",
  "timestamp": "2026-03-26T21:41:16.164908500Z",
  "data": {
    "id": 4,
    "winner": "Jaxon Smith-Njigba",
    "loser": "Josh Allen",
    "scare_difference": 2.13,
    "advantage": "SLIGHT_EDGE",
    "explanation": "Jaxon Smith-Njigba wins the matchup because of: Elite vertical threat; capable of taking the top off any defense",
    "player_1_scare_result": {
      "name": "Josh Allen",
      "nfl_team": "BUF",
      "position": "QB",
      "scare_score": 93.24,
      "scare_tier": "ELITE",
      "primary_explanation": "Elite goal-line weapon",
      "supporting_explanations": [
        "Aggressive finisher; consistently finds the end zone",
        "Elite passing QB"
      ]
    },
    "player_2_scare_result": {
      "name": "Jaxon Smith-Njigba",
      "nfl_team": "SEA",
      "position": "WR",
      "scare_score": 95.38,
      "scare_tier": "ELITE",
      "primary_explanation": "Elite vertical threat; capable of taking the top off any defense",
      "supporting_explanations": [
        "Dangerous scoring threat; consistently finds paydirt",
        "High-volume possession receiver with dependable hands"
      ]
    }
  }
}
```

</details>
<details>
<summary><b>Team Matchup Result</b> (<code>POST /api/matchup/team/create</code>)</summary>

```json
{
  "status": "SUCCESS",
  "message": "Operation successful",
  "timestamp": "2026-03-26T21:46:47.536599600Z",
  "data": {
    "id": 2,
    "team_1": "First team",
    "team_2": "Second team",
    "team_1_score": 251.98,
    "team_2_score": 263.07,
    "team_1_win_probability": 36.49,
    "team_2_win_probability": 63.51,
    "advantage": "SLIGHT_EDGE",
    "player_matchups": [
      {
        "id": 5,
        "winner": "Drake London",
        "loser": "George Pickens",
        "scare_difference": 0.04,
        "advantage": "EVEN"
      },
      {
        "id": 6,
        "winner": "Malik Nabers",
        "loser": "Jalen Hurts",
        "scare_difference": 1.77,
        "advantage": "EVEN"
      },
      {
        "id": 7,
        "winner": "Nico Collins",
        "loser": "Joe Burrow",
        "scare_difference": 12.9,
        "advantage": "CLEAR_EDGE"
      }
    ]
  }
}
```

</details>

## 📦 Tech Stack

- **Backend:** Java 17, Spring Boot, Hibernate (JPA)
- **Database:** MySQL (Persistent storage), H2 (Testing)
- **Testing:** JUnit 5, Cucumber (BDD), RestAssured
- **DevOps:** Docker, Docker Compose, Jenkins (CI/CD)
- **Documentation:** OpenAPI / Swagger UI

## 🚀 Getting Started

### Prerequisites

- [Docker](https://www.docker.com/products/docker-desktop/)
- [Java 17+](https://adoptium.net/temurin/releases/?version=17) *(only required for local development without Docker)*

---

### Option 1: Run with Docker (Recommended)

1. **Clone the repository**
   ```bash
   git clone https://github.com/Jamal-Karim/NFL_Fantasy_Matchup_Analyzer.git
   cd NFL_Fantasy_Matchup_Analyzer
   ```

2. **Configure Environment**
   Copy the environment file and update your database credentials:
   ```bash
   cp .env.example .env
   ```

3. **Start the Application**
   ```bash
   docker-compose up --build -d
   ```

#### This will start:

- Spring Boot application on port `8081`
- MySQL database on port `3307`

No additional setup is required — Docker handles all dependencies and configuration.

---

### Option 2: Run Locally (For Development)

1. **Start MySQL via Docker**
   ```bash
   docker-compose up -d mysqldb
   ```

2. **Configure Environment**

   Copy the application properties file and update your database credentials:
    ```bash
    cp src/main/resources/application.properties.example src/main/resources/application.properties
    ```

   **Optional** (to use live NFL data instead of the mock provider):
    - add your RapidAPI key to `application.properties`
    - set `spring.profiles.active=prod` in `application.properties`


3. **Run the Application**
   ```bash
   # On macOS/Linux/Git Bash
   ./mvnw spring-boot:run
   
   # On Windows Command Prompt
   mvnw.cmd spring-boot:run
   ```

### 🔍 Verification

Once the application starts, you can verify functionality by:

- **Swagger UI:** [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
    - Explore and test the API endpoints directly.
- **Mock Data:** The system initializes with a default set of players.
    - Try calling the analysis endpoint for Bijan Robinson (ID: 22).
- **Postman Collection:** Import the collection located in `/docs/api` to explore prebuilt requests.

## 🔮 Future Improvements

- Develop a frontend interface for interactive user workflows
- Integrate real-time NFL data sources permanently for improved accuracy
- Expand simulation to full season projections and trends analysis
- Enhance matchup explanations with deeper analytics