package com.mozzartbet.gameservice.parser;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.mozzartbet.gameservice.GameServiceApplicationTests;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.util.ConvertHelper;

public class TeamParserTest extends GameServiceApplicationTests {

  @Autowired
  TeamParserBasketballRef teamParser;

  @Test
  public void testPlayerNumber() {
    // TeamParserBasketballRef teamParser = new TeamParserBasketballRef();
    Team team = teamParser.returnTeamLocal("teamsBOS2019");
    int playerNumber = -1;
    for (Player p : team.getPlayers()) {
      if (p.getName().equals("Al Horford"))
        playerNumber = ConvertHelper.tryParseInt(p.getNumber());
    }
    assertEquals(42, playerNumber);
  }
}
