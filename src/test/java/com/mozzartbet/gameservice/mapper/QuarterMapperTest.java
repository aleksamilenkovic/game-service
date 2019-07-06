package com.mozzartbet.gameservice.mapper;

import static org.junit.Assert.assertEquals;
import java.time.LocalDateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import com.mozzartbet.gameservice.GameServiceApplicationTests;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.Quarter;
import com.mozzartbet.gameservice.domain.Season;
import com.mozzartbet.gameservice.domain.Team;
import lombok.extern.slf4j.Slf4j;

@ActiveProfiles("test")
@Transactional
@Slf4j
public class QuarterMapperTest extends GameServiceApplicationTests {
  @Autowired
  private QuarterMapper quarterMapper;
  @Autowired
  private MatchMapper matchMapper;
  @Autowired
  private TeamMapper teamMapper;

  @Test
  public void testCrud() throws Exception {
    log.info("counting empty quarters");
    assertEquals(0L, quarterMapper.count());
    Season season = Season.builder().seasonYear(2019).build();
    Team t1 = Team.builder().name("KK Toplicanin").teamId("TOP/2019").build(),
        t2 = Team.builder().name("KK Prokuplje").teamId("PK/2019").build();
    teamMapper.insert(t1);
    teamMapper.insert(t2);
    Match m = Match.builder().matchId("SAC21015154").awayTeam(t2).homeTeam(t1).finalScore("119-120")
        .dateTime(LocalDateTime.now().minusDays(30)).awayTeamPoints(119).seasonYear(2019).build();
    matchMapper.insert(m);
    Quarter quarter =
        Quarter.builder().match(m).name("1st Q").pointsAwayTeam(119).pointsHomeTeam(110).build();
    log.info("saving quarter to database");
    assertEquals(1, quarterMapper.insert(quarter));

    log.info("Fetching date from quarters");
    Quarter qFromDB = quarterMapper.getById(quarter.getId());

    assertEquals("1st Q", qFromDB.getName());
  }
}
