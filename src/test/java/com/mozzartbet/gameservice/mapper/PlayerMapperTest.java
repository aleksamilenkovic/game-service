package com.mozzartbet.gameservice.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.mozzartbet.gameservice.GameServiceApplicationTests;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.domain.Team;
import lombok.extern.slf4j.Slf4j;

@Transactional
@Slf4j
public class PlayerMapperTest extends GameServiceApplicationTests {
  @Autowired
  private PlayerMapper playerMapper;
  @Autowired
  private TeamMapper teamMapper;

  @Test
  public void testCrud() {
    assertEquals(0, playerMapper.count());
    Team t = Team.builder().name("Boston Celtics").teamId("BOS/2019").build();
    teamMapper.save(t);

    Player p =
        Player.builder().team(Team.builder().teamId(("BOS/2019")).build()).name("Kyrie Irving")
            .number("3").birthDate(LocalDateTime.now()).college("college").experience(8)
            .playerId("kyrie").position("FG").height("7\"7").build(),
        p2 = Player.builder().team(Team.builder().teamId(("BOS/2019")).build()).name("Aleksa Kralj")
            .number("3").birthDate(LocalDateTime.now()).college("University of Belgrade")
            .experience(8).playerId("aleksam").position("FG").height("6\"9").build();
    playerMapper.save(p2);
    log.info("Saving the player");
    assertEquals(playerMapper.save(p), 1);
    log.info("Reloading the player");
    Player player = playerMapper.getById("kyrie");
    assertEquals("FG", player.getPosition());
    // log.info("DELETING THE PLAYER");
    // assertEquals(playerMapper.deleteById(player.getId()), 1);

    log.info("reloading list of players from team");
    List<Player> playersFromTeam = playerMapper.getPlayersForTeam("BOS/2019");
    assertFalse(
        playersFromTeam.size() != 2 && !playersFromTeam.get(1).getName().equals("Aleksa Kralj"));
  }
}
