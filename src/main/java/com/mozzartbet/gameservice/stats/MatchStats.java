package com.mozzartbet.gameservice.stats;

import java.util.ArrayList;
import java.util.List;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.domain.boxscore.PlayerStats;
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

  public void calculateTeamStatsOnMatch(Match match, Team team) {

    if (team.getTeamName().equals(match.getAwayTeam())
        || team.getTeamName().equals(match.getHomeTeam())) {
      this.homeTeamName = match.getHomeTeam();
      this.awayTeamName = match.getAwayTeam();
      this.matchId = match.getMatchId();

      if (match.getMatchEvents() != null && team.getPlayers() != null) {
        for (Player p : team.getPlayers()) {
          PlayerStats ps =
              StatisticCaclulator.calculatePlayerStats(match.getMatchEvents(), p.getId());
          if (team.getTeamName().equals(homeTeamName))
            homeTeamPlayerStats.add(ps);
          else
            awayTeamPlayerStats.add(ps);
          // System.out.println(ps);
          // playersStats.put(p.getId(), ps);
          // teamStatsSummary.set
        }
      } else
        System.out.println("MatchEvents || teamPlayers is null ");
    }

  }

  public void summary() {
    // playersStats.forEach((key, value) -> System.out.println(key + " = " + value));
    // teamStatsSummary.
    /*
     * for (Entry<String, PlayerStats> entry : playersStats.entrySet()) { PlayerStats ps =
     * entry.getValue(); teamStatsSummary.setAssists(ps.getAssists() +
     * teamStatsSummary.getAssists()); teamStatsSummary.setBlocks(ps.getBlocks() +
     * teamStatsSummary.getBlocks()); teamStatsSummary.setDefensiveRebounds(
     * ps.getDefensiveRebounds() + teamStatsSummary.getDefensiveRebounds());
     * teamStatsSummary.setFieldGoalAttempts( ps.getFieldGoalAttempts() +
     * teamStatsSummary.getFieldGoalAttempts()); teamStatsSummary.setFieldGoals(ps.getFieldGoals() +
     * teamStatsSummary.getFieldGoals()); teamStatsSummary.setFreeThrowAttempts(
     * ps.getFreeThrowAttempts() + teamStatsSummary.getFreeThrowAttempts());
     * teamStatsSummary.setFreeThrows(ps.getFreeThrows() + teamStatsSummary.getFreeThrows());
     * teamStatsSummary.setOffensiveRebounds( ps.getOffensiveRebounds() +
     * teamStatsSummary.getOffensiveRebounds()); teamStatsSummary
     * .setPersonalFouls(ps.getPersonalFouls() + teamStatsSummary.getPersonalFouls());
     * teamStatsSummary.setPoints(ps.getPoints() + teamStatsSummary.getPoints());
     * teamStatsSummary.setSteals(ps.getSteals() + teamStatsSummary.getSteals());
     * teamStatsSummary.setThreePointFG(ps.getThreePointFG() + teamStatsSummary.getThreePointFG());
     * teamStatsSummary.setThreePointFGAttempts( ps.getThreePointFGAttempts() +
     * teamStatsSummary.getThreePointFGAttempts()); teamStatsSummary
     * .setTotalRebounds(ps.getTotalRebounds() + teamStatsSummary.getTotalRebounds());
     * teamStatsSummary.setTurnovers(ps.getTurnovers() + teamStatsSummary.getTurnovers());
     * 
     * 
     * } teamStatsSummary.setFreeThrowPercentage(ConvertHelper.roundDecimal( ((double)
     * teamStatsSummary.getFreeThrows() / teamStatsSummary.getFreeThrowAttempts()), 3));
     * teamStatsSummary.setFieldGoalPercentage(ConvertHelper.roundDecimal( ((double)
     * teamStatsSummary.getFieldGoals() / teamStatsSummary.getFieldGoalAttempts()), 3));
     * teamStatsSummary.setThreePointFGPercentage(ConvertHelper.roundDecimal( ((double)
     * teamStatsSummary.getThreePointFG() / teamStatsSummary.getThreePointFGAttempts()), 3));
     */
  }
}
