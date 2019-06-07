package com.mozzartbet.gameservice.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import java.util.List;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.Season;
import com.mozzartbet.gameservice.exception.UrlException;

public class MatchTests {


  @Test
  public void testMatchNotNull() {
    MatchParser jmp = new MatchParser();
    Match match = jmp.returnMatch("201112260PHO", null);
    // assertEquals(match.getMatchEvents().get(0).getTimestamp(), "12:00.0");
    assertThat(match, IsNull.notNullValue());
  }


  @Test
  public void testMatchFinalScore() {
    MatchParser jmp = new MatchParser();
    Match match = jmp.returnMatch("201905200POR", "pbp201905200POR");
    assertEquals(match.getFinalScore(), "119 - 117");
  }


  @Test
  public void testNumberOfMatchesInMonth() {
    MatchParser jmp = new MatchParser();
    List<Match> matches = jmp.returnMatchesFromMonth(2012, "december");
    assertEquals(56, matches.size()); // System.out.println(matches); }
  }

  /*
   * ovo je dug test posto vraca celu sezonu live pa je pod komentarom da ga ne bih pokretao svaki
   * put
   */

  // @Test
  public void testReturningSeason() throws UrlException {
    MatchParser jmp = new MatchParser();
    Season s = null;
    s = jmp.returnSeasonMatches(2012);
    assertEquals(s.getSeasonYear(), 2012);
  }



}
