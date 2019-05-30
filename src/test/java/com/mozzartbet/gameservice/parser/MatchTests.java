package com.mozzartbet.gameservice.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.Season;
import com.mozzartbet.gameservice.domain.boxscore.PlayerStats;
import com.mozzartbet.gameservice.stats.StatisticCaclulator;

public class MatchTests {


  @Test
  public void testMatchNotNull() {
    MatchParser jmp = new MatchParser();
    Match match = jmp.returnMatch("201905200POR",
        "Portland Trail Blazers at Golden State Warriors Play-By-Play, May 16, 2019 _ Basketball-Reference.com");
    // assertEquals(match.getMatchEvents().get(0).getTimestamp(), "12:00.0");
    assertThat(match, IsNull.notNullValue());
  }

  @Test
  public void testPlayerStats() {
    MatchParser jmp = new MatchParser();
    Match match = jmp.returnMatch("201905160GSW",
        "Portland Trail Blazers at Golden State Warriors Play-By-Play, May 16, 2019 _ Basketball-Reference.com");
    PlayerStats ps =
        StatisticCaclulator.calculatePlayerStats(match.getMatchEvents(), "k/kanteen01", false);
    // System.out.println(ps);
  }

  @Test
  public void testMatchFinalScore() {
    MatchParser jmp = new MatchParser();
    Match match = jmp.returnMatch("201904100CHO",
        "Portland Trail Blazers at Golden State Warriors Play-By-Play, May 16, 2019 _ Basketball-Reference.com");
    assertEquals(match.getFinalScore(), "111 - 114");
  }

  /*
   * @Test public void testNumberOfMatchesInMonth() { MatchParser jmp = new MatchParser();
   * LinkedList<Match> matches = jmp.returnMatchesFromMonth(2001, "october");
   * assertEquals(matches.size(), 13); // System.out.println(matches); }
   * 
   */
  /*
   * ovo je dug test posto vraca celu sezonu live pa je pod komentarom da ga ne bih pokretao svaki
   * put
   */

  // @Test
  public void testReturningSeason() {
    MatchParser jmp = new MatchParser();
    Season s = jmp.returnSeasonMatches(2012);
    assertEquals(s.getSeasonYear(), 2012);
  }

}
