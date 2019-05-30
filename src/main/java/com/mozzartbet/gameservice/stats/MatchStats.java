package com.mozzartbet.gameservice.stats;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.domain.boxscore.PlayerStats;
import com.mozzartbet.gameservice.util.ConvertHelper;
import lombok.Data;

@Data
public class MatchStats {
  private String matchId;
  private String awayTeamName;
  private String homeTeamName;
  private Table<String, String, Integer> lineScore = HashBasedTable.create(); // zbir poena po
                                                                              // cetvrtinama i
                                                                              // timu
  // private Map<String, PlayerStats> playersStats;// statistika za svakog igraca posebno na mecu iz
  private List<PlayerStats> homeTeamPlayerStats;
  private List<PlayerStats> awayTeamPlayerStats;
  private PlayerStats awayTeamStatsSummary; // statistika po timu
  private PlayerStats homeTeamStatsSummary;
  // u teamStatsSummary se smesta summary stats tima jer je model isti samo za playerId stavljam
  // teamName

  public MatchStats() {
    homeTeamPlayerStats = new ArrayList<PlayerStats>();
    awayTeamPlayerStats = new ArrayList<PlayerStats>();
  }

  public void calculateMatchStats(Match match, Team team1, Team team2) {
    StatisticCaclulator.lineScore = HashBasedTable.create();
    calculateTeamStatsOnMatch(match, team1);
    calculateTeamStatsOnMatch(match, team2);
    lineScore = StatisticCaclulator.lineScore;
    summaryStats();
  }

  public void calculateTeamStatsOnMatch(Match match, Team team) {
    if (team.getTeamName().equals(match.getAwayTeam())
        || team.getTeamName().equals(match.getHomeTeam())) {
      this.homeTeamName = match.getHomeTeam();
      StatisticCaclulator.homeTeam = match.getHomeTeam();
      this.awayTeamName = match.getAwayTeam();
      StatisticCaclulator.awayTeam = match.getAwayTeam();
      this.matchId = match.getMatchId();

      if (match.getMatchEvents() != null && team.getPlayers() != null) {
        ArrayList<PlayerStats> playersStats = StatisticCaclulator
            .returnTeamStatsIndividual(match.getMatchEvents(), team.getPlayers());
        playersStats.remove(playersStats.size() - 1);
        if (team.getTeamName().equals(homeTeamName)) {
          homeTeamPlayerStats.addAll(playersStats);
        } else
          awayTeamPlayerStats.addAll(playersStats);

      }
    } else
      System.out.println("MatchEvents || teamPlayers is null ");
  }


  public void sortByPoints() {
    awayTeamPlayerStats.sort((Comparator.comparing(PlayerStats::getPoints)).reversed());
    homeTeamPlayerStats.sort((Comparator.comparing(PlayerStats::getPoints)).reversed());
  }

  public void sortByAssist() {
    awayTeamPlayerStats.sort((Comparator.comparing(PlayerStats::getPoints)).reversed());
    homeTeamPlayerStats.sort((Comparator.comparing(PlayerStats::getPoints)).reversed());
  }

  private void summaryStats() {
    summaryAwayTeam();
    summaryHomeTeam();
  }

  private void summaryAwayTeam() {
    for (PlayerStats ps : awayTeamPlayerStats) {
      awayTeamStatsSummary.setAssists(ps.getAssists() + awayTeamStatsSummary.getAssists());
      awayTeamStatsSummary.setBlocks(ps.getBlocks() + awayTeamStatsSummary.getBlocks());
      awayTeamStatsSummary.setDefensiveRebounds(
          ps.getDefensiveRebounds() + awayTeamStatsSummary.getDefensiveRebounds());
      awayTeamStatsSummary.setFieldGoalAttempts(
          ps.getFieldGoalAttempts() + awayTeamStatsSummary.getFieldGoalAttempts());
      awayTeamStatsSummary.setFieldGoals(ps.getFieldGoals() + awayTeamStatsSummary.getFieldGoals());
      awayTeamStatsSummary.setFreeThrowAttempts(
          ps.getFreeThrowAttempts() + awayTeamStatsSummary.getFreeThrowAttempts());
      awayTeamStatsSummary.setFreeThrows(ps.getFreeThrows() + awayTeamStatsSummary.getFreeThrows());
      awayTeamStatsSummary.setOffensiveRebounds(
          ps.getOffensiveRebounds() + awayTeamStatsSummary.getOffensiveRebounds());
      awayTeamStatsSummary
          .setPersonalFouls(ps.getPersonalFouls() + awayTeamStatsSummary.getPersonalFouls());
      awayTeamStatsSummary.setPoints(ps.getPoints() + awayTeamStatsSummary.getPoints());
      awayTeamStatsSummary.setSteals(ps.getSteals() + awayTeamStatsSummary.getSteals());
      awayTeamStatsSummary
          .setThreePointFG(ps.getThreePointFG() + awayTeamStatsSummary.getThreePointFG());
      awayTeamStatsSummary.setThreePointFGAttempts(
          ps.getThreePointFGAttempts() + awayTeamStatsSummary.getThreePointFGAttempts());
      awayTeamStatsSummary
          .setTotalRebounds(ps.getTotalRebounds() + awayTeamStatsSummary.getTotalRebounds());
      awayTeamStatsSummary.setTurnovers(ps.getTurnovers() + awayTeamStatsSummary.getTurnovers());


    }
    awayTeamStatsSummary.setFreeThrowPercentage(
        ConvertHelper.roundDecimal(((double) awayTeamStatsSummary.getFreeThrows()
            / awayTeamStatsSummary.getFreeThrowAttempts()), 3));
    awayTeamStatsSummary.setFieldGoalPercentage(
        ConvertHelper.roundDecimal(((double) awayTeamStatsSummary.getFieldGoals()
            / awayTeamStatsSummary.getFieldGoalAttempts()), 3));
    awayTeamStatsSummary.setThreePointFGPercentage(
        ConvertHelper.roundDecimal(((double) awayTeamStatsSummary.getThreePointFG()
            / awayTeamStatsSummary.getThreePointFGAttempts()), 3));

  }

  private void summaryHomeTeam() {
    for (PlayerStats ps : homeTeamPlayerStats) {
      homeTeamStatsSummary.setAssists(ps.getAssists() + homeTeamStatsSummary.getAssists());
      homeTeamStatsSummary.setBlocks(ps.getBlocks() + homeTeamStatsSummary.getBlocks());
      homeTeamStatsSummary.setDefensiveRebounds(
          ps.getDefensiveRebounds() + homeTeamStatsSummary.getDefensiveRebounds());
      homeTeamStatsSummary.setFieldGoalAttempts(
          ps.getFieldGoalAttempts() + homeTeamStatsSummary.getFieldGoalAttempts());
      homeTeamStatsSummary.setFieldGoals(ps.getFieldGoals() + homeTeamStatsSummary.getFieldGoals());
      homeTeamStatsSummary.setFreeThrowAttempts(
          ps.getFreeThrowAttempts() + homeTeamStatsSummary.getFreeThrowAttempts());
      homeTeamStatsSummary.setFreeThrows(ps.getFreeThrows() + homeTeamStatsSummary.getFreeThrows());
      homeTeamStatsSummary.setOffensiveRebounds(
          ps.getOffensiveRebounds() + homeTeamStatsSummary.getOffensiveRebounds());
      homeTeamStatsSummary
          .setPersonalFouls(ps.getPersonalFouls() + homeTeamStatsSummary.getPersonalFouls());
      homeTeamStatsSummary.setPoints(ps.getPoints() + homeTeamStatsSummary.getPoints());
      homeTeamStatsSummary.setSteals(ps.getSteals() + homeTeamStatsSummary.getSteals());
      homeTeamStatsSummary
          .setThreePointFG(ps.getThreePointFG() + homeTeamStatsSummary.getThreePointFG());
      homeTeamStatsSummary.setThreePointFGAttempts(
          ps.getThreePointFGAttempts() + homeTeamStatsSummary.getThreePointFGAttempts());
      homeTeamStatsSummary
          .setTotalRebounds(ps.getTotalRebounds() + homeTeamStatsSummary.getTotalRebounds());
      homeTeamStatsSummary.setTurnovers(ps.getTurnovers() + homeTeamStatsSummary.getTurnovers());


    }
    awayTeamStatsSummary.setFreeThrowPercentage(
        ConvertHelper.roundDecimal(((double) awayTeamStatsSummary.getFreeThrows()
            / awayTeamStatsSummary.getFreeThrowAttempts()), 3));
    awayTeamStatsSummary.setFieldGoalPercentage(
        ConvertHelper.roundDecimal(((double) awayTeamStatsSummary.getFieldGoals()
            / awayTeamStatsSummary.getFieldGoalAttempts()), 3));
    awayTeamStatsSummary.setThreePointFGPercentage(
        ConvertHelper.roundDecimal(((double) awayTeamStatsSummary.getThreePointFG()
            / awayTeamStatsSummary.getThreePointFGAttempts()), 3));

  }


}
