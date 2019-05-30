package com.mozzartbet.gameservice.parser;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import org.junit.Test;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.domain.boxscore.PlayerStats;
import com.mozzartbet.gameservice.stats.StatisticCaclulator;

public class StatsTests {
  @Test
  public void testTeamStats() {
    // TEST DA LI JE LILARD NA MECU DAO 23 POENA
    MatchParser matchParse = new MatchParser();
    TeamParser teamParse = new TeamParser();
    Match match = matchParse.returnMatch("201905160GSW",
        "Portland Trail Blazers at Golden State Warriors Play-By-Play, May 16, 2019 _ Basketball-Reference.com");
    Team team = teamParse.returnTeam("https://www.basketball-reference.com/teams/POR/2019.html",
        "Portland Trail Blazers");
    ArrayList<PlayerStats> playersStats =
        StatisticCaclulator.returnTeamStatsIndividual(match.getMatchEvents(), team.getPlayers());
    for (PlayerStats ps : playersStats) {
      System.out.println(ps);
    }
    assertEquals(playersStats.get(0).getPoints(), 23);
  }
}
