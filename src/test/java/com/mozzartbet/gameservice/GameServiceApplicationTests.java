package com.mozzartbet.gameservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.mozzartbet.gameservice.parser.JunitTest;
import com.mozzartbet.gameservice.parser.MatchParser;
import com.mozzartbet.gameservice.parser.StatsTests;
import com.mozzartbet.gameservice.parser.TeamParser;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameServiceApplicationTests {
  @Test
  public void contextLoads() {
    MatchParser matchParse = new MatchParser();
    TeamParser teamParse = new TeamParser();
    JunitTest unitTest = new JunitTest();
    StatsTests st = new StatsTests();
    // TeamTests tt = new TeamTests();
    // unitTest.testNumberOfPlayers();
    // unitTest.testNumberOfSeasons(2010);
    // unitTest.testMatchRead();
    // unitTest.testPlayerInATeam("Peja Stojakovic", "Sacramento", 2000);

    // TESTING THE WRONG URL EXCEPTION
    // unitTest.testMatchPbpFirstRow("wrongUrl", null);


    // unitTest.testMatchPbpFirstRow("201905160GSW",
    // "Portland Trail Blazers at Golden State Warriors Play-By-Play, May 16, 2019 _
    // Basketball-Reference.com");
    // TEST DA U OKTOBRU 2019 JE BILO 110 utakmica
    // unitTest.testNumberOfMatchesInMonth(2001, "October", 13);
    // unitTest.testReturningSeason(2001);

    /*
     * Match match = matchParse.returnMatch("201905160GSW",
     * "Portland Trail Blazers at Golden State Warriors Play-By-Play, May 16, 2019 _ Basketball-Reference.com"
     * ); Team team =
     * teamParse.returnTeam("https://www.basketball-reference.com/teams/POR/2019.html", "Portland");
     * st.testTeamStats(match, team);
     */
  }

}
