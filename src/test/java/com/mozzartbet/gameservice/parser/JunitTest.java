package com.mozzartbet.gameservice.parser;

import static org.junit.Assert.assertEquals;
import java.util.LinkedList;
import org.junit.Test;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.MatchEvent;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.domain.Season;
import com.mozzartbet.gameservice.domain.Team;

public class JunitTest {

  @Test
  public void testNumberOfPlayers() {
    JSoupTeamParser jp = new JSoupTeamParser();
    Team tm = jp.returnTeam("https://www.basketball-reference.com/teams/TOR/2019.html",
        "TORONTO RAPTORS");
    assertEquals(16, tm.getPlayers().size());
  }

  @Test
  public void testPlayerInATeam(String player, String team, int year) {
    // Testira da li je neki igrac bio u nekom timu u odredjenoj sezoni
    JSoupTeamParser jp = new JSoupTeamParser();
    LinkedList<Team> teams = jp.readTeamsFromSeason(year);
    for (Team t : teams)
      if (t.getTeamName().equals(team))
        for (Player p : t.getPlayers())
          assertEquals(p.getName(), player);

  }

  @Test
  public void testNumberOfSeasons(int startingYear) {
    // metoda proverava broj sezona od odredjene godine
    // test je napravljen zbog provere koliko metodi readTeamsFromSpecificSeasonTillNow
    // treba vremena da se izvrsi i provera da nema exepction-a
    JSoupTeamParser jp = new JSoupTeamParser();
    LinkedList<LinkedList<Team>> allSeasonTeams =
        jp.readTeamsFromSpecificSeasonTillNow(startingYear);
    int size = allSeasonTeams.size();
    assertEquals(size, 2020 - startingYear);
  }

  @Test
  public void testMatchPbpFirstRow() {
    JSoupMatchParser jmp = new JSoupMatchParser();
    LinkedList<MatchEvent> matchEvents = jmp
        .returnMatchEvents("https://www.basketball-reference.com/boxscores/pbp/201905200POR.html");
    assertEquals(matchEvents.get(0).getTimestamp(), "12:00.0");
  }

  @Test
  public void testNumberOfMatchesInMonth(String urlOfMonth, int numberOfMatches) {
    JSoupMatchParser jmp = new JSoupMatchParser();
    LinkedList<Match> matches = jmp.returnMatchesFromMonth(urlOfMonth);
    assertEquals(matches.size(), numberOfMatches);
  }

  public void testReturningSeason(int year) {
    JSoupMatchParser jmp = new JSoupMatchParser();
    Season s = jmp.returnSeasonMatches(year);
    assertEquals(s.getSeasonYear(), year);
  }

}
