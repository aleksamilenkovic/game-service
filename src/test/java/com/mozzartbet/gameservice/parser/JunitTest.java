package com.mozzartbet.gameservice.parser;

import static org.junit.Assert.assertEquals;
import java.util.LinkedList;
import org.junit.Test;
import com.mozzartbet.gameservice.domain.Player;

public class JunitTest {

  @Test
  public void testNumberOfPlayers() {
    JSoupParser jp = new JSoupParser();
    LinkedList<Player> list =
        jp.returnPlayers("https://www.basketball-reference.com/teams/TOR/2019.html");
    assertEquals(16, list.size());


  }

}
