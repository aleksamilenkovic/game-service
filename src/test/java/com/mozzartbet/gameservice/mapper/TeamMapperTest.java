package com.mozzartbet.gameservice.mapper;

import static org.junit.Assert.assertEquals;
import java.time.LocalDateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.mozzartbet.gameservice.GameServiceApplicationTests;
import com.mozzartbet.gameservice.domain.Season;
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.parser.TeamParser;
import lombok.extern.slf4j.Slf4j;

@Transactional
@Slf4j
public class TeamMapperTest extends GameServiceApplicationTests {
  @Autowired
  private TeamMapper teamMapper;

  @Autowired
  private TeamParser teamParser;

  @Autowired
  private PlayerMapper playerMapper;
  /*
   * @Autowired private TeamService teamService;
   * 
   * @Test public void testTeamService() { teamService.saveTeam("teamsBOS2019"); Team t =
   * teamService.getTeam("BOS/2019"); assertEquals("Boston Celtics", t.getTeamName()); }
   */


  @Test
  public void returnAllTeamsAndPlayersFromSeason() {
    Season season =
        Season.builder().seasonYear(2019).teams(teamParser.readTeamsFromSeason(2019)).build();

    season.getTeams().forEach((team) -> {
      teamMapper.save(team);
      team.getPlayers().forEach((player) -> playerMapper.save(player));
    });
    assertEquals(season.getTeams().size(), 30);
    assertEquals(season.getTeams().get(4).getPlayers().size(), 17);
  }

  @Test
  public void testCrud() throws Exception {
    assertEquals(teamMapper.count(), 0L);

    LocalDateTime now = LocalDateTime.now();

    log.info("adding new team");
    Team t = Team.builder().name("Denver Nuggets").teamId("DEN/2019").build();
    assertEquals(1, teamMapper.save(t));

    log.info("Reloading a team");
    Team team = teamMapper.getById("DEN/2019");
    assertEquals("Denver Nuggets", team.getName());
    team.setName("Fafafa");

    log.info("Updating the team");
    assertEquals(teamMapper.update(team), 1);

    log.info("Again reloading updated team");
    Team ttt = teamMapper.getById("DEN/2019");
    assertEquals(ttt.getName(), "Fafafa");

    log.info("Deleting the team...");
    teamMapper.deleteById(team.getId());
    assertEquals(teamMapper.count(), 0);
  }

}
