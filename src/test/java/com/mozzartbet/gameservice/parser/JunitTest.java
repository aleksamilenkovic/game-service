package com.mozzartbet.gameservice.parser;

import static org.junit.Assert.assertEquals;
import java.util.LinkedList;
import org.junit.Test;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.Season;

public class JunitTest {



  @Test
  public void testMatchPbpFirstRow(String matchID, String fileName) {
    MatchParser jmp = new MatchParser();
    Match match = jmp.returnMatch(matchID, fileName);
    assertEquals(match.getMatchEvents().get(0).getTimestamp(), "12:00.0");
  }


  @Test
  public void testNumberOfMatchesInMonth(int year, String month, int numberOfMatches) {
    MatchParser jmp = new MatchParser();
    LinkedList<Match> matches = jmp.returnMatchesFromMonth(year, month);
    assertEquals(matches.size(), numberOfMatches);
    // System.out.println(matches);
  }

  public void testReturningSeason(int year) {
    MatchParser jmp = new MatchParser();
    Season s = jmp.returnSeasonMatches(year);
    assertEquals(s.getSeasonYear(), year);
  }

}
