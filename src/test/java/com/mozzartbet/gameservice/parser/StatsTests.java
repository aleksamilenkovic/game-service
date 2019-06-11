package com.mozzartbet.gameservice.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.boxscore.PlayerStats;
import com.mozzartbet.gameservice.stats.MatchStats;
import com.mozzartbet.gameservice.stats.StatisticCaclulator;

public class StatsTests {

  @Test
  public void testPlayerStats() {
    MatchParser matchParse = new MatchParser();
    // TeamParser teamParse = new TeamParser();
    Match match = matchParse.returnMatch("201905160GSW", "pbp201905200POR");
    /*
     * Team team = teamParse.returnTeam("https://www.basketball-reference.com/teams/POR/2019.html",
     * "Portland Trail Blazers"); Team team2 =
     * teamParse.returnTeam("https://www.basketball-reference.com/teams/GSW/2019.html",
     * "Golden State Warriors");
     */
    // team.getPlayers().addAll(team2.getPlayers());
    Map<String, PlayerStats> playersStats = StatisticCaclulator.returnPlayersStatsIndividual(match,
        match.getHomePlayersID(), match.getAwayPlayersID());
    assertEquals(playersStats.get("c/curryst01").getPoints(), 37);
    // System.out.println(ps);
  }

  @Test
  public void testTeamStats() {
    // TEST DA LI JE LILARD NA MECU DAO 23 POENA
    MatchParser matchParse = new MatchParser();
    // TeamParser teamParse = new TeamParser();
    Match match = matchParse.returnMatch("201905160GSW", "pbp201905200POR");
    /*
     * Team team = teamParse.returnTeam("https://www.basketball-reference.com/teams/POR/2019.html",
     * "Portland Trail Blazers"); Team team2 =
     * teamParse.returnTeam("https://www.basketball-reference.com/teams/GSW/2019.html",
     * "Golden State Warriors");
     */
    // team.getPlayers().addAll(team2.getPlayers());
    Map<String, PlayerStats> playersStats = (HashMap<String, PlayerStats>) StatisticCaclulator
        .returnPlayersStatsIndividual(match, match.getHomePlayersID(), match.getAwayPlayersID());
    /*
     * for (PlayerStats ps : playersStats) { System.out.println(ps); }
     */
    System.out.println(playersStats);
    // da li je broj
    assertFalse(playersStats == null);
    // assertEquals(playersStats.get(0).getPoints(), 23);
  }

  @Test
  public void testMatchStats() {
    MatchParser matchParse = new MatchParser();
    // +TeamParser teamParse = new TeamParser();
    Match match = matchParse.returnMatch("201906100TOR", null);
    /*
     * Team team1 = teamParse.returnTeam("https://www.basketball-reference.com/teams/POR/2019.html",
     * "Portland Trail Blazers"); Team team2 =
     * teamParse.returnTeam("https://www.basketball-reference.com/teams/GSW/2019.html",
     * "Golden State Warriors");
     */
    MatchStats stats = new MatchStats();
    stats.calculateMatchStats(match, "");

    assertEquals(stats.getHomeTeamStatsSummary().getThreePointFG(), 8);

  }

}
