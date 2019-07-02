package com.mozzartbet.gameservice.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import com.mozzartbet.gameservice.GameServiceApplicationTests;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.domain.Team;
import lombok.extern.slf4j.Slf4j;

@ActiveProfiles("test")
@Transactional
@Slf4j
public class PlayerMapperTest extends GameServiceApplicationTests {
  @Autowired
  private PlayerMapper playerMapper;
  @Autowired
  private TeamMapper teamMapper;

  @Test
  public void testCrud() {
    // assertEquals(0, playerMapper.count());
    Team t = Team.builder().name("KK Toplicanin").teamId("TOP/2019").seasonYear(2019).build();
    teamMapper.insert(t);

    Player p =
        Player.builder().team(t).name("Kyrie Irving").number("3").birthDate(LocalDateTime.now())
            .college("college").experience(8).playerId("irving").position("FG").height("7\"7")
            .weight("210lbs").build(),
        p2 = Player.builder().team(t).name("Aleksa Kralj").number("3")
            .birthDate(LocalDateTime.now()).college("University of Belgrade").experience(8)
            .playerId("aleksam").position("FG").height("6\"9").weight("195lbs").nationality("srb")
            .build();
    // playerMapper.count();
    log.info("SAVING THE PLAYER 1");
    playerMapper.insert(p);
    log.info("Saving the player");
    assertEquals(playerMapper.insert(p2), 1);
    log.info("Reloading the player");
    Player player = playerMapper.getByPlayerIdAndTeamId("irving", "TOP/2019");
    assertEquals("FG", player.getPosition());
    // log.info("DELETING THE PLAYER");
    // assertEquals(playerMapper.deleteById(player.getId()), 1);

    log.info("reloading list of players from team");
    List<Player> playersFromTeam = playerMapper.getPlayersForTeam("TOP/2019");
    assertFalse(
        playersFromTeam.size() != 2 && !playersFromTeam.get(1).getName().equals("Aleksa Kralj"));
    log.info("GET TEAM WITH PLAYERS");
    Team teaaam = playerMapper.getTeamWithPlayers("TOP/2019");
    log.info(teaaam.toString());
  }
}
