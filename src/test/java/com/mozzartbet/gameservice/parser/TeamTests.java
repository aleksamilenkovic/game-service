package com.mozzartbet.gameservice.parser;

import static org.junit.Assert.assertEquals;
import java.util.List;
import org.junit.Test;
import com.google.common.collect.Multimap;
import com.mozzartbet.gameservice.GameServiceApplicationTests;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.util.ConvertHelper;

public class TeamTests extends GameServiceApplicationTests {
  @Test
  public void testNumberOfPlayers() {
    TeamParserBasketballRef jp = new TeamParserBasketballRef();
    Team tm = jp.returnTeamLive("https://www.basketball-reference.com/teams/BOS/2017.html");
    assertEquals(15, tm.getPlayers().size());
  }

  @Test
  public void testPlayerInATeam() {
    // Testira da li je neki igrac bio u nekom timu u odredjenoj sezoni
    TeamParserBasketballRef jp = new TeamParserBasketballRef();
    Team team = jp.returnTeamLive("https://www.basketball-reference.com/teams/SAC/2000.html");
    Player p = team.getPlayers().get(9);
    assertEquals(p.getName(), "Peja Stojakovic");

  }

  @Test
  public void testPlayerNumber() {
    TeamParserBasketballRef teamParse = new TeamParserBasketballRef();
    Team team = teamParse.returnTeamLocal("teamsBOS2019");
    int playerNumber = -1;
    for (Player p : team.getPlayers()) {
      if (p.getName().equals("Al Horford"))
        playerNumber = ConvertHelper.tryParseInt(p.getNumber());
    }
    assertEquals(42, playerNumber);
  }

  @Test
  public void returnSeasonTeams() {
    TeamParserBasketballRef teamParse = new TeamParserBasketballRef();
    List<Team> teams = teamParse.readTeamsFromSeason(2018);
    assertEquals(teams.size(), 30);
  }

  // @Test
  public void testNumberOfSeasons() { // metoda proverava broj sezona od
    // odredjene godine // test je napravljen zbog provere koliko metodi
    TeamParserBasketballRef tp = new TeamParserBasketballRef();
    Multimap<Integer, List<Team>> teams = tp.readTeamsFromSpecificSeasonTillNow(2012);
    // treba vremena da se izvrsi i provera da nema exepction-a
    int size = teams.size();
    assertEquals(size, 2020 - 2012);
  }

}
