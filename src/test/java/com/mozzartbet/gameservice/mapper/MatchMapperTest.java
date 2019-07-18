package com.mozzartbet.gameservice.mapper;

import static org.junit.Assert.assertEquals;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import com.mozzartbet.gameservice.GameServiceApplicationTests;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.Quarter;
import com.mozzartbet.gameservice.domain.Season;
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.exception.UrlException;
import com.mozzartbet.gameservice.parser.MatchParserBasketballRef;
import lombok.extern.slf4j.Slf4j;

@ActiveProfiles("test")
@Transactional
@Slf4j
public class MatchMapperTest extends GameServiceApplicationTests {

  @Autowired
  private TeamMapper teamMapper;
  @Autowired
  private MatchMapper matchMapper;

  private MatchParserBasketballRef matchParser = new MatchParserBasketballRef();

  @Autowired
  private PlayerMapper playerMapper;
  @Autowired
  private MatchEventMapper eventMapper;
  @Autowired
  private QuarterMapper quarterMapper;
  @Autowired
  private SeasonMapper seasonMapper;

  @Test
  @Commit
  public void testMatchAndEvents() {
    Season season = Season.builder().seasonYear(2019).build();
    seasonMapper.insert(season);
    Match match = matchParser.returnMatch("201812260BRK", null);
    matchMapper.insert(match);
    match.getQuarters().forEach(quarter -> {
      quarterMapper.insert(quarter);
      quarter.getMatchEvents().forEach(event -> eventMapper.insert(event));
    });
  }

  @Test
  @Commit
  public void testMonthMatchesAndEvents() throws UrlException {

    Season season = Season.builder().seasonYear(2019)
        .seasonMatches(matchParser.returnMatchesFromMonth(2019, "december")).build();
    seasonMapper.insert(season);
    /*
     * season.setTeams(teamParser.readTeamsFromSeason(2019)); season.getTeams().forEach((team) -> {
     * teamMapper.save(team); team.getPlayers().forEach((player) -> playerMapper.save(player)); });
     */
    season.getSeasonMatches().forEach(match -> {
      matchMapper.insert(match);
      match.getQuarters().forEach(quarter -> {
        quarterMapper.insert(quarter);
        quarter.getMatchEvents().forEach(event -> {
          try {
            eventMapper.insert(event);
          } catch (Exception e) {
            // AKO SE DESI DA JE IGRAC PRESAO U TIM U TOKU SEZONE, PA GA NEMA U TIMOVIMA ALI GA IMA
            // U ROSTERU
            if (event.getFirstPlayer() != null
                & playerMapper.getByPlayerIdAndTeamId(event.getFirstPlayer().getPlayerId(),
                    event.getFirstPlayer().getTeam().getTeamId()) == null) {
              event.getFirstPlayer()
                  .setTeam(teamMapper.getByTeamId(event.getFirstPlayer().getTeam().getTeamId()));
              playerMapper.insert(event.getFirstPlayer());
            } else if (event.getSecondPlayer() != null
                && playerMapper.getByPlayerIdAndTeamId(event.getSecondPlayer().getPlayerId(),
                    event.getSecondPlayer().getTeam().getTeamId()) == null) {
              event.getSecondPlayer()
                  .setTeam(teamMapper.getByTeamId(event.getSecondPlayer().getTeam().getTeamId()));
              playerMapper.insert(event.getSecondPlayer());
            }

          }
        });
      });
    });

  }

  @Test
  public void testMatchWithQuarters() {
    Season season = Season.builder().seasonYear(2019).build();
    seasonMapper.insert(season);
    Team t1 = Team.builder().name("KK Toplicanin").teamId("TOP/2019").seasonYear(2019).build();
    Team t2 = Team.builder().name("KK Prokuplje").teamId("PK/2019").seasonYear(2019).build();
    teamMapper.insert(t1);
    teamMapper.insert(t2);
    Match m = Match.builder().matchId("SAC21015154").awayTeam(t2).homeTeam(t1).finalScore("119-120")
        .dateTime(LocalDateTime.now().minusDays(30)).awayTeamPoints(119).build();
    matchMapper.insert(m);
    Quarter quarter1 =
        Quarter.builder().match(m).name("1st Q").pointsAwayTeam(119).pointsHomeTeam(110).build(),
        quarter2 = Quarter.builder().match(m).name("2nd Q").pointsAwayTeam(151).pointsHomeTeam(111)
            .build();
    quarterMapper.insert(quarter1);
    quarterMapper.insert(quarter2);
    log.info("getting match with quarters");
    List<Quarter> quarters = matchMapper.getQuartersByMatchId("SAC21015154");
    Match match = matchMapper.getWithQuartersByMatchId("SAC21015154");
    assertEquals("2nd Q", match.getQuarters().get(1).getName());
  }

  @Test
  public void testCrud() {
    Season season = Season.builder().seasonYear(2019).build();
    seasonMapper.insert(season);
    Team t1 = Team.builder().name("KK Toplicanin").teamId("TOP/2019").seasonYear(2019).build();
    Team t2 = Team.builder().name("KK Prokuplje").teamId("PK/2019").seasonYear(2019).build();
    teamMapper.insert(t1);
    teamMapper.insert(t2);
    Match m = Match.builder().matchId("SAC21015154").awayTeam(t2).homeTeam(t1).finalScore("119-120")
        .dateTime(LocalDateTime.now().minusDays(30)).awayTeamPoints(119).build();
    log.info("COUNTING EMPTY DATABASE OF MATCHES");
    matchMapper.count();
    log.info("SAVING MATCH TO DATABSE");
    matchMapper.insert(m);
    log.info("GETTING THE MATCH");
    Match match = matchMapper.getByMatchId("SAC21015154");
    log.info(
        match.getMatchId() + match.getAwayTeam() + match.getFinalScore() + match.getHomeTeam());
    log.info("UPDATE MATCH");
    match.setAwayTeamPoints(112);
    match.setHomeTeamPoints(99);
    match.setFinalScore("112-99");
    matchMapper.update(match);
    log.info(matchMapper.getByMatchId("SAC21015154").getFinalScore());
    log.info("DELETING MATCH");
    matchMapper.deleteById(match.getId());
  }
}
