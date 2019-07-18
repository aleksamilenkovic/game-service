package com.mozzartbet.gameservice.mapper;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import com.mozzartbet.gameservice.GameServiceApplicationTests;
import com.mozzartbet.gameservice.domain.MatchEvent;
import com.mozzartbet.gameservice.domain.Season;
import com.mozzartbet.gameservice.exception.UrlException;
import com.mozzartbet.gameservice.parser.MatchParserBasketballRef;
import com.mozzartbet.gameservice.parser.TeamParserBasketballRef;
import lombok.extern.slf4j.Slf4j;

@ActiveProfiles("test")
@Transactional
@Slf4j
public class SeasonMapperTest extends GameServiceApplicationTests {
  @Autowired
  private SeasonMapper seasonMapper;
  @Autowired
  private TeamMapper teamMapper;
  @Autowired
  private PlayerMapper playerMapper;
  @Autowired
  private MatchMapper matchMapper;
  @Autowired
  private MatchEventMapper eventMapper;
  @Autowired
  private QuarterMapper quarterMapper;
  @Autowired
  private PlayerStatsMapper psMapper;

  @Test
  public void testCrud() {
    log.info("Counting players stats...");
    assertEquals(0, seasonMapper.count());
    Season season = Season.builder().seasonYear(1910).build();
    log.info("Saving the season");
    assertEquals(1, seasonMapper.insert(season));
    log.info("Fetching the season");
    Season s = seasonMapper.getById(season.getId());
    assertEquals(1910, s.getSeasonYear());
    Season seasonByYear = seasonMapper.getByYear(1910);
    assertEquals(1910, s.getSeasonYear());
    log.info("Deleting season from database");
    seasonMapper.deleteById(s.getId());
    log.info("Counting empty databse");
    assertEquals(0, seasonMapper.count());
  }

  @Test
  @Commit
  public void testSeasons() {
    // saving all seasons first
    for (int i = 1960; i <= 2019; i++) {
      seasonMapper.insert(Season.builder().seasonYear(i).build());
    }
  }

  @Test
  @Commit
  public void testSeasonWithAllMatchesAndTeams() throws UrlException {
    MatchParserBasketballRef matchParser = new MatchParserBasketballRef();
    TeamParserBasketballRef teamParser = new TeamParserBasketballRef();
    Season season = matchParser.returnSeasonWithMatches(2019);
    season.setTeams(teamParser.readTeamsFromSeason(2019));
    seasonMapper.insert(season);
    season.getTeams().forEach(team -> {
      teamMapper.insert(team);
      team.getPlayers().forEach(player -> playerMapper.insert(player));
    });
    season.getSeasonMatches().forEach(match -> {
      if (match != null) {
        matchMapper.insert(match);
        match.getQuarters().forEach(quarter -> {
          quarterMapper.insert(quarter);
          quarter.getMatchEvents().forEach(event -> {
            insertEvent(event);
          });
        });
        match.getMatchStats().getAwayTeamPlayerStats().forEach(ps -> {
          try {
            psMapper.insert(ps);
          } catch (Exception e) {
            log.info(e.toString() + "\nError with saving player stats in database for:"
                + ps.getPlayer().getPlayerId());
          }
        });
        match.getMatchStats().getHomeTeamPlayerStats().forEach(ps -> {
          try {
            psMapper.insert(ps);
          } catch (Exception e) {
            log.info(e.toString() + "\nError with saving player stats in database for:"
                + ps.getPlayer().getPlayerId());
          }
        });
      }
    });

  }

  private void insertEvent(MatchEvent event) {
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
      eventMapper.insert(event);
    }
  }
}
