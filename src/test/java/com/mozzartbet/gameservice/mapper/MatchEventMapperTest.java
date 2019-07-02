package com.mozzartbet.gameservice.mapper;

import static org.junit.Assert.assertEquals;
import java.time.LocalDateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import com.mozzartbet.gameservice.GameServiceApplicationTests;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.MatchEvent;
import com.mozzartbet.gameservice.domain.MatchEventType;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.domain.Quarter;
import com.mozzartbet.gameservice.domain.Team;
import lombok.extern.slf4j.Slf4j;

@ActiveProfiles("test")
@Transactional
@Slf4j
public class MatchEventMapperTest extends GameServiceApplicationTests {
  @Autowired
  private MatchEventMapper eventMapper;
  @Autowired
  private PlayerMapper playerMapper;
  @Autowired
  private TeamMapper teamMapper;
  @Autowired
  private MatchMapper matchMapper;
  @Autowired
  private QuarterMapper quarterMapper;

  @Test
  public void testCrud() {
    Team t1 = Team.builder().name("KK Toplicanin").teamId("TOP/2019").seasonYear(2019).build(),
        t2 = Team.builder().name("KK Prokuplje").teamId("PK/2019").seasonYear(2019).build();
    teamMapper.insert(t1);
    teamMapper.insert(t2);
    Player p = Player.builder().team(t1).name("Marko petrovic").number("3")
        .birthDate(LocalDateTime.now()).college("college").experience(8).playerId("mare")
        .position("FG").height("7\"7").weight("210lbs").nationality("us").build();
    playerMapper.insert(p);
    Match m = Match.builder().matchId("SAC21015154").awayTeam(t2).homeTeam(t1).finalScore("119-120")
        .dateTime(LocalDateTime.now().minusDays(30)).awayTeamPoints(119).build();
    matchMapper.insert(m);
    Quarter quarter =
        Quarter.builder().match(m).name("1st Q").pointsAwayTeam(119).pointsHomeTeam(110).build();
    quarterMapper.insert(quarter);


    MatchEvent event = MatchEvent.builder().quarter(quarter).awayTeamAction("afafaeefaa")
        .pointsMade(3).firstPlayer(p).eventType(MatchEventType.FOUL).eventTime(31F).build();
    log.info("Counting elements in database");
    assertEquals(0, eventMapper.count());

    log.info("Savnig in database");
    eventMapper.insert(event);

    log.info("Fetching event from db");
    MatchEvent eventtt = eventMapper.getById(event.getId());
    assertEquals(event, eventtt);

    log.info("Deleting event");
    eventMapper.deleteById(eventtt.getId());

    log.info("count empty db");
    assertEquals(0L, eventMapper.count());

  }

}
