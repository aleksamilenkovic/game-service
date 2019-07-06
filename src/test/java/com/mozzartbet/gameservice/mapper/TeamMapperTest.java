package com.mozzartbet.gameservice.mapper;

import static org.junit.Assert.assertEquals;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import com.google.common.collect.Multimap;
import com.mozzartbet.gameservice.GameServiceApplicationTests;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.domain.Season;
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.parser.TeamParser;
import lombok.extern.slf4j.Slf4j;


@ActiveProfiles("test")
@Transactional
@Slf4j
public class TeamMapperTest extends GameServiceApplicationTests {
  @Autowired
  private TeamMapper teamMapper;

  @Autowired
  private TeamParser teamParser;

  @Autowired
  private PlayerMapper playerMapper;
  @Autowired
  private SeasonMapper seasonMapper;


  @Test
  public void returnAllTeamsAndPlayersFromSeason() {
    Season season =
        Season.builder().seasonYear(2016).teams(teamParser.readTeamsFromSeason(2016)).build();

    season.getTeams().forEach((team) -> {
      teamMapper.insert(team);
      team.getPlayers().forEach((player) -> playerMapper.insert(player));
    });
    assertEquals(season.getTeams().size(), 30);
    assertEquals(season.getTeams().get(4).getPlayers().size(), 18);
    List<Player> players = teamMapper.getPlayersForTeam("BOS/2016");
    Team tt = teamMapper.getWithPlayersByTeamId("BOS/2016");
    assertEquals(17, players.size());
  }

  @Test
  @Commit
  public void saveAllTeamsAndPlayersForAllSeasons() {

    Multimap<Integer, List<Team>> allTeams = teamParser.readTeamsFromSpecificSeasonTillNow(2010);
    for (List<Team> teams : allTeams.values()) {
      teams.forEach(team -> {
        teamMapper.insert(team);
        team.getPlayers().forEach(player -> playerMapper.insert(player));
      });
    }

    assertEquals(300, teamMapper.count());
  }

  @Test
  public void testCrud() throws Exception {

    assertEquals(teamMapper.count(), 0L);
    Season season = Season.builder().seasonYear(2019).build();
    seasonMapper.insert(season);
    LocalDateTime now = LocalDateTime.now();

    log.info("adding new team");
    Team t = Team.builder().name("Denver Nuggets").teamId("DEN/2019").seasonYear(2019).build();
    assertEquals(1, teamMapper.insert(t));

    log.info("Reloading a team");
    Team team = teamMapper.getByTeamId("DEN/2019");
    assertEquals("Denver Nuggets", team.getName());
    team.setName("Fafafa");

    log.info("Updating the team");
    assertEquals(teamMapper.update(team), 1);

    log.info("Again reloading updated team");
    Team ttt = teamMapper.getByTeamId("DEN/2019");
    assertEquals(ttt.getName(), "Fafafa");

    log.info("Deleting the team...");
    // teamMapper.deleteById(team.getId());
    // assertEquals(teamMapper.count(), 0);


  }

}
