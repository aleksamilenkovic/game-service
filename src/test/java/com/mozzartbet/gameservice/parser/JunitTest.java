package com.mozzartbet.gameservice.parser;

import static org.junit.Assert.assertEquals;
import java.util.List;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.Season;
import com.mozzartbet.gameservice.exception.UrlException;

public class JunitTest {


  // @Test
  public void testMatchPbpFirstRow(String matchID, String fileName) {
    MatchParserBasketballRef jmp = new MatchParserBasketballRef();
    Match match = jmp.returnMatch(matchID, fileName);
    assertEquals(match.getQuarters().get(0).getMatchEvents().get(0).getEventTime(), "12:00.0");
  }


  // @Test
  public void testNumberOfMatchesInMonth(int year, String month, int numberOfMatches) {
    MatchParserBasketballRef jmp = new MatchParserBasketballRef();
    List<Match> matches = jmp.returnMatchesFromMonth(year, month);
    assertEquals(matches.size(), numberOfMatches);
    // System.out.println(matches);
  }

  public void testReturningSeason(int year) throws UrlException {
    MatchParserBasketballRef jmp = new MatchParserBasketballRef();
    Season s = null;
    s = jmp.returnSeasonMatches(year);
    assertEquals(s.getSeasonYear(), year);
  }



}
