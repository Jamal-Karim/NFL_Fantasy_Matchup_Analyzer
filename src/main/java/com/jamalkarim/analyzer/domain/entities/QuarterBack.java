package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.PlayerStats;
import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.stats.BlendedStats;

import java.util.*;

public class QuarterBack extends Player{

    public static final double MAX_PASSING_YARDS_PER_GAME = 235.0;
    public static final double MAX_PASSING_TDS_PER_GAME = 2.0;
    public static final double MAX_INTERCEPTIONS_PER_GAME = 1.0;
    public static final double MAX_RUSHING_YARDS_PER_GAME = 50.0;
    public static final double MAX_RUSHING_TDS_PER_GAME = 0.8;

    public static final double PASSING_YARDS_WEIGHT = 0.15;
    public static final double PASSING_TDS_WEIGHT = 0.35;
    public static final double INT_WEIGHT = 0.05;
    public static final double COMPLETION_WEIGHT = 0.05;
    public static final double RUSHING_YARDS_WEIGHT = 0.15;
    public static final double RUSHING_TDS_WEIGHT = 0.35;

    public QuarterBack(String name, String team) {
        super(name, team, Position.QB);
    }

    @Override
    public double calculateScareFactor() {
        Map<PlayerStats, Impact> impactMap = generateImpactMap();

        double score = 0.0;

        score += impactMap.get(PlayerStats.PassingTDs).getPointsGained();
        score += impactMap.get(PlayerStats.PassingYards).getPointsGained();
        score += impactMap.get(PlayerStats.Completion).getPointsGained();

        score -= impactMap.get(PlayerStats.Interceptions).getPointsGained();

        score += impactMap.get(PlayerStats.RushingTDs).getPointsGained();
        score += impactMap.get(PlayerStats.RushingYards).getPointsGained();

        return Math.max(0, applySoftCap(score * 125));
    }

    protected Map<PlayerStats, Impact> generateImpactMap(){
        Map<PlayerStats, Impact> impactMap = new HashMap<>();
        BlendedStats stats = calculateStatBlendStrategy();

        double passingYardsRatio = stats.getPassingYardsPerGame() / MAX_PASSING_YARDS_PER_GAME;
        double passingTDsRatio = stats.getPassingTDsPerGame() / MAX_PASSING_TDS_PER_GAME;
        double intsRatio = stats.getIntsPerGame() / MAX_INTERCEPTIONS_PER_GAME;
        double rushingYardsRatio = stats.getRushingYardsPerGame() / MAX_RUSHING_YARDS_PER_GAME;
        double rushingTDsRatio = stats.getRushingTDsPerGame() / MAX_RUSHING_TDS_PER_GAME;
        double completionPct = (stats.getPassAttemptsPerGame() > 0)
                ? stats.getCompletionsPerGame() / stats.getPassAttemptsPerGame()
                : 0.0;

        impactMap.put(PlayerStats.PassingYards, generateImpactForStat(passingYardsRatio, PASSING_YARDS_WEIGHT));

        impactMap.put(PlayerStats.PassingTDs, generateImpactForStat(passingTDsRatio, PASSING_TDS_WEIGHT));

        impactMap.put(PlayerStats.Interceptions, generateImpactForStat(intsRatio, INT_WEIGHT));

        impactMap.put(PlayerStats.RushingYards, generateImpactForStat(rushingYardsRatio, RUSHING_YARDS_WEIGHT));

        impactMap.put(PlayerStats.RushingTDs, generateImpactForStat(rushingTDsRatio, RUSHING_TDS_WEIGHT));

        impactMap.put(PlayerStats.Completion, generateImpactForStat(completionPct, COMPLETION_WEIGHT));

        return impactMap;
    }


    @Override
    public List<String> generateListOfExplanations() {
        Map<PlayerStats, Double> topContributingFactors = findTopContributingScores(generateImpactMap());
        List<String> explanations = new ArrayList<>();

        for(Map.Entry<PlayerStats, Double> entry : topContributingFactors.entrySet()){
            PlayerStats stat = entry.getKey();
            Double value = entry.getValue();
            switch (stat){
                case PassingYards -> explanations.add(getPassingYardsLabel(getTierForStatistic(value, PASSING_YARDS_WEIGHT)));
                case PassingTDs -> explanations.add(getPassingTDsLabel(getTierForStatistic(value, PASSING_TDS_WEIGHT)));
                case Interceptions -> explanations.add(getIntsLabel(getTierForStatistic(value, INT_WEIGHT)));
                case Completion -> explanations.add(getCompletionsLabel(getTierForStatistic(value, COMPLETION_WEIGHT)));
                case RushingYards -> explanations.add(getRushingYardsLabel(getTierForStatistic(value, RUSHING_YARDS_WEIGHT)));
                case RushingTDs -> explanations.add(getRushingTDsLabel(getTierForStatistic(value, RUSHING_TDS_WEIGHT)));
            }
        }
        return explanations;
    }

    private String getPassingYardsLabel(int tier) {
        return switch (tier) {
            case 1  -> "Elite passing QB";
            case 2  -> "Highly productive pocket general with strong yardage output";
            case 3  -> "Efficient move-the-chains passer; reliable volume";
            case -1 -> "Struggles to find rhythm; limited downfield production";
            case -2 -> "Inconsistent air attack; frequently under pressure";
            case -3 -> "Conservative passing approach with low explosive potential";
            default -> "Average passing impact";
        };
    }

    private String getPassingTDsLabel(int tier) {
        return switch (tier) {
            case 1  -> "Elite red zone sniper; high-frequency scoring threat";
            case 2  -> "Aggressive finisher; consistently finds the end zone";
            case 3  -> "Steady contributor in scoring situations";
            case -1 -> "Red zone liability; struggles to capitalize on scoring drives";
            case -2 -> "Low touchdown frequency; offense stalls in the red zone";
            case -3 -> "Difficulty finishing drives through the air";
            default -> "Average passing touchdown impact";
        };
    }

    private String getIntsLabel(int tier) {
        return switch (tier) {
            case 1  -> "Elite ball security; rarely puts the ball in harm's way";
            case 2  -> "Disciplined decision-maker; avoids costly turnovers";
            case 3  -> "Generally safe with the ball; manages risk well";
            case -1 -> "Erratic gunslinger; high turnover risk in tight windows";
            case -2 -> "Prone to critical mistakes; struggles with defensive reads";
            case -3 -> "Frequent turnovers are a major drag on offensive momentum";
            default -> "Average interception risk";
        };
    }

    private String getCompletionsLabel(int tier) {
        return switch (tier) {
            case 1  -> "Surgical precision; consistently hits receivers in stride";
            case 2  -> "High-accuracy passer; keeps the offense on schedule";
            case 3  -> "Reliable completion rate in a standard offensive scheme";
            case -1 -> "Erratic ball placement; frequently misses open targets";
            case -2 -> "Inaccurate under pressure; struggles with timing routes";
            case -3 -> "Low-efficiency passer; often off-target on routine throws";
            default -> "Average accuracy";
        };
    }

    private String getRushingYardsLabel(int tier) {
        return switch (tier) {
            case 1  -> "Elite dual-threat scrambler";
            case 2  -> "Dangerous runner out of the pocket";
            case 3  -> "Capable of picking up yards on the ground";
            case -1 -> "Strictly a pocket passer; zero mobility";
            case -2 -> "Limited mobility; heavy reliance on protection";
            case -3 -> "Primarily stays in the pocket";
            default -> "Average rushing impact";
        };
    }

    private String getRushingTDsLabel(int tier) {
        return switch (tier) {
            case 1  -> "Elite goal-line weapon";
            case 2  -> "Dynamic red zone runner";
            case 3  -> "Can be used to pick up a touchdown";
            case -1 -> "Never used as an option in the red zone";
            case -2 -> "Rarely used to pick up touchdowns";
            case -3 -> "More likely to pass than run in the red zone";
            default -> "Average rushing red zone impact";
        };
    }
}
