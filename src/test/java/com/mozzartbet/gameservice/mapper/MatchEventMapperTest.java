package com.mozzartbet.gameservice.mapper;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.mozzartbet.gameservice.GameServiceApplicationTests;
import com.mozzartbet.gameservice.domain.MatchEvent;
import com.mozzartbet.gameservice.domain.MatchEventType;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.domain.Team;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MatchEventMapperTest extends GameServiceApplicationTests {
  @Autowired
  private MatchEventMapper eventMapper;
  @Autowired
  private PlayerMapper playerMapper;
  @Autowired
  private TeamMapper teamMapper;

  @Test
  public void testCrud() {
    Team t = Team.builder().name("BOSTON").teamId("BOS/2019").build();
    teamMapper.save(t);
    Player p = Player.builder().playerId("Aleska").college("University of Belgrade").name("Aleksa")
        .team(t).build();
    playerMapper.save(p);
    MatchEvent event = MatchEvent.builder().matchId("BOS/2019").awayTeamAction("afafaeefaa")
        .pointsMade(3).firstPlayerId("Aleska").eventType(MatchEventType.SHOOTFOR2).build();
    log.info("Counting elements in database");
    assertEquals(0, eventMapper.count());

    log.info("Savnig in database");
    eventMapper.save(event);

    log.info("Fetching event from db");
    MatchEvent eventtt = eventMapper.getById(1L);
    assertEquals(event, eventtt);

    log.info("Deleting event");
    eventMapper.deleteById(eventtt.getId());

    log.info("count empty db");
    assertEquals(0L, eventMapper.count());;
  }
}
