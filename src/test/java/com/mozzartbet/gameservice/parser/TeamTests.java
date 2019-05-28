package com.mozzartbet.gameservice.parser;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.domain.Team;

public class TeamTests {
  @Test
  public void testNumberOfPlayers() {
    TeamParser jp = new TeamParser();
    Team tm = jp.returnTeam("https://www.basketball-reference.com/teams/TOR/2019.html",
        "TORONTO RAPTORS");
    assertEquals(16, tm.getPlayers().size());
  }

  @Test
  public void testPlayerInATeam() {
    // Testira da li je neki igrac bio u nekom timu u odredjenoj sezoni
    TeamParser jp = new TeamParser();
    Team team =
        jp.returnTeam("https://www.basketball-reference.com/teams/SAC/2000.html", "Sacramento");
    Player p = team.getPlayers().get(9);
    assertEquals(p.getName(), "Peja Stojakovic");

  }

  /*
   * @Test public void testNumberOfSeasons(int startingYear) { // metoda proverava broj sezona od
   * odredjene godine // test je napravljen zbog provere koliko metodi
   * readTeamsFromSpecificSeasonTillNow // treba vremena da se izvrsi i provera da nema exepction-a
   * TeamParser jp = new TeamParser(); LinkedList<LinkedList<Team>> allSeasonTeams =
   * jp.readTeamsFromSpecificSeasonTillNow(startingYear); int size = allSeasonTeams.size();
   * assertEquals(size, 2020 - startingYear); }
   */
}
