package com.mozzartbet.gameservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.mozzartbet.gameservice.parser.JunitTest;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameServiceApplicationTests {
  @Test
  public void contextLoads() {
    JunitTest unitTest = new JunitTest();
    // unitTest.testNumberOfPlayers();
    // unitTest.testNumberOfSeasons(2010);
    // unitTest.testMatchRead();
    // unitTest.testPlayerInATeam("Peja Stojakovic", "Sacramento", 2000);
    // unitTest.testMatchPbpFirstRow();
    // TEST DA U OKTOBRU 2019 JE BILO 110 utakmica
    // unitTest.testNumberOfMatchesInMonth(
    // "https://www.basketball-reference.com/leagues/NBA_2019_games-october.html", 110);
    unitTest.testReturningSeason(1967);

  }

}
