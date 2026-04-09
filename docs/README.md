# 📚 Documentation Hub

This folder contains in-depth technical documentation for the NFL Fantasy Analyzer system, including architecture
design, data flow, simulation logic, and API usage.

## 🗺️ Architectural Diagrams

These diagrams visualize the core architecture and decision-making logic within the application. They are maintained
using **Mermaid.js**,
ensuring the visuals stay aligned with the actual implementation.

| Diagram                                                               | Description                                                             |
|:----------------------------------------------------------------------|:------------------------------------------------------------------------|
| [**System Architecture**](./diagrams/system-architecture.md)          | High-level overview of the API, Service, and Data layers.               |
| [**Data Provider Flow**](./diagrams/data-providers.md)                | Visualizes the "Sync-on-Demand" strategy for local vs. external data.   |
| [**Monte Carlo Engine**](./diagrams/monte-carlo-simulation.md)        | Deep dive into the 10,000-iteration simulation logic.                   |
| [**Request Lifecycle**](./diagrams/team-matchup-request-lifecycle.md) | Sequence diagram showing the end-to-end flow of a team matchup request. |

## 📡 API Documentation

* [**Postman Collection**](./api/NFL%20Fantasy%20API%20Analyzer.postman_collection.json) — Contains pre-configured
  requests, environment variables, and example responses for all endpoints.

## 📈 Model Analysis & Evaluation

This section contains the raw data and visualizations used to evaluate the **Scare Factor** model against the 2025 NFL
season.

* [**Evaluation Data (CSV)**](./analysis/fantasy_2025_month_1.csv) — Week-by-week breakdown of predictions vs. actual
  outcomes (first month of the 2025 NFL season).
* [**Performance Visualization**](./analysis/Model%20Performance%20of%20Application%20vs%20Platform.png) — Comparison
  chart of App accuracy vs. traditional platform projections.
* [**Confidence Calibration**](./analysis/Model%20Accuracy%20by%20Confidence%20Level.png) — Analysis of how the model
  performs at different probability thresholds.

## 🏗️ Design Artifacts

* [Architecture Rough Draft](./architecture/Fantasy%20Football%20Analyzer%20Application%20Rough%20Draft%20Architecture.png) —
  Early-stage design sketch illustrating initial system planning and component relationships.
